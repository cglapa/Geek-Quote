package com.supinfo.geekquote.model;

import java.io.Serializable;
import java.util.Date;

public class Quote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8417515498929205131L;
	private String strQuote;
	private int rating;
	private Date creationDate;
	
	public String getStrQuote() {
		return strQuote;
	}
	public void setStrQuote(String strQuote) {
		this.strQuote = strQuote;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
