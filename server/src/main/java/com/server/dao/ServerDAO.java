package com.server.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hibernate.FbData;
import com.hibernate.HibernateUtil;
import com.server.exceptions.DataBaseException;
import com.server.exceptions.FbDataRequestException;



public class ServerDAO {
	Logger logger = Logger.getLogger(ServerDAO.class);
	Session session = null;
	
	/*Get data from DB. 
	 * Option == 1 -- > In case that this pageID is already on the DB. Then use the last date that the request (to facebook) was made and just get the new data.
	 * Option == 2 -- > Group the "likes" and "shares" by the time (of post creation).
	 */
	public List<FbData> getDataDB(String idPage, List<FbData> dbData, Integer option) throws DataBaseException{
		PropertyConfigurator.configure("log4j.properties");
		logger.info("- DAO - Get into method -> getDataDB(), to get Db data");
		List<Object[]> tmpDbData = null;
		 
		Date day = new Date();
		SimpleDateFormat date3 = new SimpleDateFormat("y-MM-d k:mm:ss");
		Long amountDaysBefore = 10L;
		Date daysBefore = new Date(day.getTime() - amountDaysBefore * 24 * 3600 * 1000);
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
		    session.beginTransaction();
		    Query query = null;
		

			if(option==1){
			query = session.createQuery("from FbData where idPage = :idPage order by date desc");
			query.setParameter("idPage", idPage);

			dbData = query.list();
			}else {
					

				query = session.createQuery("select sum(likes), sum(shares), time from FbData where idPage = :idPage group by time");
				query.setParameter("idPage", idPage);

				tmpDbData = (List<Object[]>) query.list();
				
				for(Object[] fbData : tmpDbData) {
					Long likes = (Long) fbData[0];
					Long shares = (Long) fbData[1];
					String time = (String) fbData[2];
					FbData data = new FbData();			
				   	data.setLikes(likes);
				   	data.setShares(shares);
				   	data.setTime(time);
				   	dbData.add(data);
					
				}
				}
			
		}catch(HibernateException e){
			
			throw new DataBaseException(e.getMessage(), 500);
			
		}finally{
			session.clear();
			session.close();
	
		}
    	
		
		return dbData;
		
	}
	
	/*Insert data from DB. 
	 * requesUrlWithDate == True -- > The idPage is not on the DB, so the request is for ALL the data from Facebook.
	 * requesUrlWithDate == False -- > Just get the new data.
	 */
	public void insertFbData(String code, String sinceDate, boolean requesUrlWithDate) throws FbDataRequestException,DataBaseException{
		PropertyConfigurator.configure("log4j.properties");
		logger.info("- DAO - Get into method -> insertFbData(), to insert data on the DB");
		String url= requesUrlWithDate==false ?  "https://graph.facebook.com/" + code +"/feed?fields=shares,likes.limit(0).summary(true),from"
				
		: "https://graph.facebook.com/" + code +"/feed?fields=shares,likes.limit(0).summary(true),from&since=" + sinceDate + "&until=now" ;
		

		Gson gson = new Gson();  	
    	JsonArray array = null;
    	JsonObject jsonFile = null;
    	
		String responseData;
		session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
    	
		try{
			
		
			do{
				responseData = facebookRequest(url);
				jsonFile = gson.fromJson(responseData, JsonObject.class);
				array = jsonFile.get("data").getAsJsonArray();
				url = mapDataToInsert(code, responseData, array, jsonFile, session);
    		
    		 
    			
			   }while(array.size()!=0);
    	
			session.getTransaction().commit();
    	
		}catch(HibernateException e){
			
			session.getTransaction().rollback();
			throw new DataBaseException(e.getMessage());
		} catch (FbDataRequestException e) {
		

			throw new FbDataRequestException(e.getMessage(), e.getCode());
		}finally{
			session.clear();
			session.close();
		}
		
    	
	}
	
	//Map the data obtained from every request to Facebook and insert all the data on the DB.
	public String mapDataToInsert(String code, String responseData, JsonArray array, JsonObject jsonFile, Session session ){
		PropertyConfigurator.configure("log4j.properties");
		logger.info("- DAO - Get into method -> mapDataToInsert(), map the data obtained by facebook");
		
		String url = "";
		JsonElement jsonElement;
		String element;
		  try {
		for(int i = 0 ; i < array.size(); i++){
			 FbData fbData = new FbData();
			 Date date = new Date();
			 fbData.setDate(date);
			 
			if(array.get(i).getAsJsonObject().get("shares")!= null){
				jsonElement = array.get(i).getAsJsonObject().get("shares").getAsJsonObject().get("count");
				element = jsonElement.toString();
				
				fbData.setShares(Long.parseLong(element));
				
			  }
			else{
				  fbData.setShares(0L);
			  }
			
			if(array.get(i).getAsJsonObject().get("likes").getAsJsonObject()!= null){
				jsonElement = array.get(i).getAsJsonObject().get("likes").getAsJsonObject().get("summary").getAsJsonObject().get("total_count");
				element = jsonElement.toString();
				fbData.setLikes(Long.parseLong(element));
				
			  }
			else{
				  fbData.setLikes(0L);
			  }
			
			if(array.get(i).getAsJsonObject().get("created_time")!= null){
				jsonElement = array.get(i).getAsJsonObject().get("created_time");
				if(!jsonElement.toString().substring(12,13).equals("0")){
					
					element = jsonElement.toString().substring(12,14);
				}else {
					element = jsonElement.toString().substring(13,14);
					
				}
				
				fbData.setTime(element);
				
			  }
			
						
				fbData.setIdPage(code);
				
			
			
			
			session.save(fbData);
			
		}
		if(jsonFile.getAsJsonObject().get("paging")!= null){
			jsonElement = jsonFile.getAsJsonObject().get("paging").getAsJsonObject().get("next");
			element = jsonElement.toString();
			url = element.substring(1,element.length()-1);
			
			
		  } 
		} catch (JsonParseException  e) {
			System.out.println("Error al parsear el archivo JSON: " + e);
			
		}
		return url;
			
	}
	//Facebook request method
	public String facebookRequest(String url) throws FbDataRequestException {
		  PropertyConfigurator.configure("log4j.properties");
		  logger.info("- DAO - Get into method -> facebookRequest(), to make the request to Facebook");
		  
		  String jsonData = "";
		  String inputLine;
		  StringBuffer response = new StringBuffer();
		  HttpURLConnection con = null;
		  URL obj;
		
				try {
					obj = new URL(url);
					con = (HttpURLConnection) obj.openConnection();
					
					con.setRequestMethod("GET");
						
					//AccessToken to make the request to Facebook.
					String accessToken = "";
					
					con.setRequestProperty("Authorization", accessToken);
					
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					
					}
					in.close();
					jsonData = response.toString();
				} catch (MalformedURLException e) {
					
					throw new FbDataRequestException("URL is wrong", 500);
				} catch(FileNotFoundException e) {

				try {
						throw new FbDataRequestException("The idPage is not correct", con.getResponseCode());
					} catch (IOException e1) {

						e.printStackTrace();
					}
				}catch (IOException e) {

					throw new FbDataRequestException("Problems with Facebook server", 500);
					
				}
				
		 con.disconnect();
		 
		  
		  return jsonData;
		  
		  
	  }
	
}
