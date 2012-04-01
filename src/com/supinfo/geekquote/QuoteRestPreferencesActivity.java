package com.supinfo.geekquote;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class QuoteRestPreferencesActivity extends PreferenceActivity implements  OnPreferenceChangeListener {
	private EditTextPreference RESTURI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.restpreferences);
		
		CheckBoxPreference RESTActivated = (CheckBoxPreference) findPreference("rest_activated");
		RESTURI = (EditTextPreference) findPreference("rest_uri");
		
		RESTURI.setEnabled(RESTActivated.isChecked());
		RESTURI.setSummary(RESTURI.getText());
		
		RESTActivated.setOnPreferenceChangeListener(this);
		RESTURI.setOnPreferenceChangeListener(this);
	}

	public boolean onPreferenceChange(Preference pref, Object newValue) {
		if("rest_uri".equals(pref.getKey())) {
			String URI = (String) newValue;
			// URI must finish with a trailing slash to work properly
			if(!"/".equals(URI.substring(URI.length()-1)))
				URI += "/";
			RESTURI.setSummary(URI);
			RESTURI.setText(URI);
			return false;
		} else {
			boolean isChecked = (Boolean) newValue;
			RESTURI.setEnabled(isChecked);
		}
		return true;
	}

}
