package com.server.exceptions;

import java.net.HttpURLConnection;

public class FbDataRequestException extends Exception {
	
	
	private static final long serialVersionUID = 1L;
	
	private HttpURLConnection con;
	private Integer code;
	
	public  FbDataRequestException(){
		
	}
	public  FbDataRequestException(Throwable cause){
		super(cause);
	}
	
	public  FbDataRequestException(String message){
		super(message);
		
	}
	
	public  FbDataRequestException(String message, Throwable cause){
		super(message, cause);
		
	}
	
	public  FbDataRequestException(String message, Throwable cause, HttpURLConnection con ){
		super(message, cause);
		this.con = con;
	}
	public  FbDataRequestException(String message, Integer code ){
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
