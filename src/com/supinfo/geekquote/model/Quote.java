package com.supinfo.geekquote.model;

import java.util.Date;

public class Quote {
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
