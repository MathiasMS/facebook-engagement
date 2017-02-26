package com.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.PrePersist;





public class FbData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer idData;
	private Long likes;
	private Long shares;
	private String time;
	private String idPage;
	private Date date;
	
	public FbData() {		
	}
	
	public FbData(Long likes, Long shares, String time, String idPage,Date date){
		this.likes = likes;
		this.shares = shares;
		this.time = time;
		this.idPage = idPage;
		this.date = date;
	}
	
	
	public Integer getIdData(){
		return idData;
		
	}
	public void setIdData(Integer idData){
		this.idData = idData;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getShares() {
		return shares;
	}
	public void setShares(Long shares) {
		this.shares = shares;
	}

	public String getIdPage() {
		return idPage;
	}

	public void setIdPage(String idPage) {
		this.idPage = idPage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	
	
}
