package com.supinfo.geekquote.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RESTPreferences {
	private SharedPreferences preferences = null;
	private String RestURI = null; 
	private Boolean RestActivated = null;
	
	public RESTPreferences(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String getRestURI() {
		if(RestURI == null) {
			if(preferences != null)
				RestURI = preferences.getString("rest_uri", "");
		}
		return RestURI;
	}
	
	public Boolean getRestActivated() {
		if(RestActivated == null) {
			if(preferences != null)
				RestActivated = preferences.getBoolean("rest_activated", true);
		}
		return RestActivated;
	}
}
