package com.server.server;
import spark.Spark;
import static spark.Spark.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.google.gson.Gson;
import com.hibernate.FbData;
import com.server.exceptions.*;
import com.server.service.ServerService;


public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	
    	Spark.staticFileLocation("public");
        port(8081);
    	Logger logger = Logger.getLogger(Server.class);
    	PropertyConfigurator.configure("log4j.properties");
    	
        HTTPErrorJson httpError = new HTTPErrorJson();
        ServerService service = new ServerService();
        Gson gson = new Gson();
       
       

    	get("/getData/:code", (request, response) -> {
    		
    	
    	
    	String idPage = request.params(":code").trim().toString();
    	
    	

    	List<FbData> dbData	= null;

    	try{
    		//Call service to look if the idPage is on the DB.
    		dbData = service.getDataDB(idPage, dbData,1);
    		
    	}catch(DataBaseException e)
    	
    	{
    		logger.error(e.getMessage(), e);
    		httpError.setCode("2");
    		httpError.setMessage("Server Error");
    		response.status(e.getCode());
    		
    	}
    	
    	//If the idPage is not on the DB.
		if(dbData.size()==0){
			
    	try{
    		//Make the request to Facebook to then insert the data.
    		service.insertFbData(String.valueOf(idPage),"", false);
    		
    	}catch(FbDataRequestException e){    		
    		logger.error(e.getMessage(), e);
    		httpError.setCode("1");
    		httpError.setMessage("Bad Request");
    		response.status(e.getCode());
    		
    	}
    	
    	
		if(response.status()==200){
			
			try{
	    		//Get the data to show on the FrontEnd
				dbData = service.getDataDB(idPage, dbData,2);
	    		
	    	}catch(DataBaseException e)
	    	
	    	{
	    		logger.error(e.getMessage(), e);
	    		httpError.setCode("2");
	    		httpError.setMessage("Server Error");
	    		response.status(e.getCode());
	    		
	    	}
			
			
			
		}
		
		}else{
			//Take the last time that the request to facebook was made, to only get the new data.
			SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMMM-y");
			String sinceDate = dateFormat.format(dbData.get(0).getDate());
			
			sinceDate = sinceDate.replace("-", "%20");
			
			try{
				
				
	    		service.insertFbData(idPage, sinceDate, true);
	    		
	    		if(response.status()==200){
	    			dbData.clear();
	    			try{
	    				//Get the data to show on the FrontEnd
	    				dbData = service.getDataDB(idPage, dbData,2);
	    	    		
	    	    	}catch(DataBaseException e)
	    	    	
	    	    	{
	    	    		logger.error(e.getMessage(), e);
	    	    		httpError.setCode("2");
	    	    		httpError.setMessage("Server Error");
	    	    		response.status(e.getCode());
	    	    		
	    	    	}
	    		}	
	    		
	    	}catch(FbDataRequestException e){	
	    		httpError.setCode("2");
	    		httpError.setMessage("Server Error");
	    		logger.error(e.getMessage(), e);
	    		response.status(e.getCode());
	    		
	    	}
			
			
			
		}
		

		
		
		String json = "";
		//Create a Json file with the data obtained.
		if(response.status()==200){
			
			json = gson.toJson(dbData);
			
		}else {
			//Create a Json file with the error response.
			json = gson.toJson(httpError);
		}
		
		
	    return json;
	});
		
    	
    		


    }

  
}
