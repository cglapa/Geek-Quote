package com.supinfo.geekquote.REST;

import java.net.URI;
import java.text.SimpleDateFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.supinfo.geekquote.R;
import com.supinfo.geekquote.model.Quote;
import com.supinfo.geekquote.preferences.RESTPreferences;

public class UpdateQuoteREST implements Runnable {
	private Quote quote;
	private JSONObject generatedJSON;
	private Toast toast;
	
	public UpdateQuoteREST(Quote quote, Toast toast) {
		this.quote = quote;
		this.toast = toast;
	}

	public void run() {
		try {
			generateJSON();
			sendToServer();
			toast.setText(R.string.update_quote_finished);
		} catch(ConnectTimeoutException e) {
			e.printStackTrace();
			toast.setText(R.string.refresh_quote_error_timedout);
		} catch(HttpHostConnectException e) {
			e.printStackTrace();
			toast.setText(R.string.refresh_quote_error_connrefused);
		} catch(JSONException e) {
			e.printStackTrace();
			toast.setText(R.string.refresh_quote_error_json);
		} catch(Exception e) {
			e.printStackTrace();
			toast.setText(R.string.refresh_quote_error_unknown);
		} finally {
			toast.show();
		}
	}
	
	private void generateJSON() throws JSONException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		generatedJSON = new JSONObject();
		
		generatedJSON.put("id", quote.getServerId());
		generatedJSON.put("strQuote", quote.getStrQuote());
		generatedJSON.put("rating", quote.getRating());
		generatedJSON.put("creationDate", dateFormatter.format(quote.getCreationDate()));
	}
	
	private void sendToServer() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpPut httpPut = new HttpPut();
		// TODO Define RESTURI inside User Interface
		URI uri = new URI(RESTPreferences.RESTURI+"quote/");
		httpPut.setURI(uri);
		httpPut.setEntity(new StringEntity(generatedJSON.toString()));
		httpPut.setHeader("Content-Type", "application/json");
		
		httpClient.execute(httpPut); 
	}
}
