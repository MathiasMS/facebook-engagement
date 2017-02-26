package com.server.service;


import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.hibernate.FbData;
import com.server.dao.ServerDAO;
import com.server.exceptions.DataBaseException;
import com.server.exceptions.FbDataRequestException;


public class ServerService {
	Logger logger = Logger.getLogger(ServerService.class);
	ServerDAO dao = new ServerDAO();
	
	 public List<FbData> getDataDB(String idPage,List<FbData> dbData,Integer option) throws DataBaseException {
		PropertyConfigurator.configure("log4j.properties");
		logger.info("- SERVICE - Get into method -> getDataDB()");
		
		try{
			
		dbData = dao.getDataDB(idPage, dbData, option);
		
		}catch(DataBaseException e){
			
			throw new DataBaseException(e.getMessage(), e.getCode());
		}
		return dbData;
	 }
	 
	 public void insertFbData(String code, String sinceDate, boolean requesUrlWithDate) throws FbDataRequestException,DataBaseException{
		 PropertyConfigurator.configure("log4j.properties");
		 logger.info("- SERVICE - Get into method -> insertFbData()");
		 
		 
		 try {
			 
			dao.insertFbData(code,  sinceDate,  requesUrlWithDate);
	
		} catch (FbDataRequestException e) {
			
			throw new FbDataRequestException(e.getMessage(), e.getCode());
			
		}catch(DataBaseException e){
			
			throw new DataBaseException(e.getMessage());
		}
		 
		}
	
	 	
}
