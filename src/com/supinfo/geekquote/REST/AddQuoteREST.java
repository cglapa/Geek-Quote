package com.supinfo.geekquote.REST;

import java.net.URI;
import java.text.SimpleDateFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.supinfo.geekquote.R;
import com.supinfo.geekquote.helper.QuoteSqliteHelper;
import com.supinfo.geekquote.model.Quote;
import com.supinfo.geekquote.preferences.RESTPreferences;

public class AddQuoteREST implements Runnable {
	private Quote quote;
	private QuoteSqliteHelper sql;
	private JSONObject generatedJSON;
	private String serverResponse;
	private Toast toast;
	private RESTPreferences preferences;
	
	public AddQuoteREST(Quote quote, QuoteSqliteHelper sql, Toast toast, RESTPreferences preferences) {
		this.quote = quote;
		this.sql = sql;
		this.toast = toast;
		this.preferences = preferences;
	}

	public void run() {
		try {
			generateJSON();
			sendToServer();
			setGeneratedId();
			toast.setText(R.string.add_quote_finished);
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
		
		generatedJSON.put("strQuote", quote.getStrQuote());
		generatedJSON.put("rating", quote.getRating());
		generatedJSON.put("creationDate", dateFormatter.format(quote.getCreationDate()));
	}
	
	private void sendToServer() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		serverResponse = null;
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpPost httpPost = new HttpPost();
		URI uri = new URI(preferences.getRestURI()+"quote/");
		httpPost.setURI(uri);
		httpPost.setEntity(new StringEntity(generatedJSON.toString()));
		httpPost.setHeader("Content-Type", "application/json");
		
		HttpResponse response = httpClient.execute(httpPost);
		serverResponse = EntityUtils.toString(response.getEntity()); 
	}
	
	private void setGeneratedId() throws JSONException {
		quote.setServerId(Long.parseLong(serverResponse));
		
		sql.updateQuote(quote);
	}
}
