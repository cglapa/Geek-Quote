package com.supinfo.geekquote;

import java.util.ArrayList;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.os.Bundle;

public class QuoteListActivity extends Activity {
	private ArrayList<Quote> quotesArray;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}