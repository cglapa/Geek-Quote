package com.supinfo.geekquote.REST;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.supinfo.geekquote.handler.RefreshQuoteHandler;
import com.supinfo.geekquote.helper.QuoteSqliteHelper;
import com.supinfo.geekquote.model.Quote;
import com.supinfo.geekquote.preferences.RESTPreferences;

public class RefreshQuoteREST implements Runnable {
	private String jsonString;
	private ArrayList<Quote> parsedJSON = new ArrayList<Quote>();
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	private RefreshQuoteHandler handler;
	private QuoteSqliteHelper sql;
	
	public RefreshQuoteREST(RefreshQuoteHandler handler, QuoteSqliteHelper sql, ArrayList<Quote> quotesArray) {
		this.handler = handler;
		this.sql = sql;
		this.quotesArray = quotesArray;
	}
	
	public void run() {
		Message message = handler.obtainMessage();
		message.arg1 = RefreshQuoteHandler.STATUS_STARTED;
		handler.sendMessage(message);
		message = handler.obtainMessage();
		try {
			getAllQuotesRaw();
			parseJSON();
			regenerateQuotes();
			message.arg1 = RefreshQuoteHandler.STATUS_FINISHED;
		} catch(ConnectTimeoutException e) { 
			e.printStackTrace();
			message.arg1 = RefreshQuoteHandler.STATUS_ERROR_TIMEDOUT;
		} catch(HttpHostConnectException e) {
			e.printStackTrace();
			message.arg1 = RefreshQuoteHandler.STATUS_ERROR_CONNREFUSED;
		} catch (JSONException e) {
			// This will be caught if JSON library can't parse server return
			e.printStackTrace();
			message.arg1 = RefreshQuoteHandler.STATUS_ERROR_JSON;
		} catch(Exception e) {
			// We've to catch any unknown exception
			e.printStackTrace();
			message.arg1 = RefreshQuoteHandler.STATUS_ERROR_UNKNOWN;
		} finally {
			handler.sendMessage(message);
		}
	}
	
	private void getAllQuotesRaw() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		jsonString = null;
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		// TODO Define RESTURI inside User Interface
		URI uri = new URI(RESTPreferences.RESTURI);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		jsonString = EntityUtils.toString(response.getEntity()); 
	}
	
	private void parseJSON() throws JSONException {
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONArray json = jsonObj.getJSONArray("quote");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int nb = json.length();
		
		JSONObject current;
		Quote quote;
		for(int i = 0;i<nb;i++) {
			current = json.getJSONObject(i);
			quote = new Quote();
			quote.setId(current.getLong("id"));
			quote.setStrQuote(current.getString("strQuote"));
			quote.setRating(current.getInt("rating"));
			try {
				quote.setCreationDate(dateFormatter.parse(current.getString("creationDate")));
			} catch (ParseException e) {
				e.printStackTrace();
				quote.setCreationDate(Calendar.getInstance().getTime());
			}
			
			parsedJSON.add(quote);
		}
	}
	
	private void regenerateQuotes() {
		String[] args = {};
		SQLiteDatabase db = sql.getWritableDatabase();
		db.delete(QuoteSqliteHelper.TABLE_NAME, "", args);
		quotesArray.clear();
		for(Quote q : parsedJSON) {
			sql.insertQuoteRaw(q, db);
			quotesArray.add(q);
		}
	}
}
