package com.server.exceptions;

import java.net.HttpURLConnection;

public class DataBaseException extends Exception{
	
	
	
	private static final long serialVersionUID = 1L;
	
	private HttpURLConnection con;
	private Integer code;
	
	public  DataBaseException(){
		
	}
	public  DataBaseException(Throwable cause){
		super(cause);
	}
	
	public  DataBaseException(String message){
		super(message);
		
	}
	
	public  DataBaseException(String message, Throwable cause){
		super(message, cause);
		
	}
	
	public  DataBaseException(String message, Throwable cause, HttpURLConnection con ){
		super(message, cause);
		this.con = con;
	}
	public  DataBaseException(String message, Integer code ){
		super(message);		
		this.code = code;
	}
	public HttpURLConnection getCon() {
		return con;
	}
	public void setCon(HttpURLConnection con) {
		this.con = con;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	

}
