package com.supinfo.geekquote;

import java.util.ArrayList;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteListActivity extends Activity {
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources();
        String quotes[] = res.getStringArray(R.array.quotes);
        for(String quote : quotes) {
        	addQuote(quote);
        }
        
        LinearLayout quotesView = (LinearLayout) findViewById(R.id.quotesview);
        for(Quote quote : quotesArray) {
        	TextView quoteView = new TextView(QuoteListActivity.this);
        	quoteView.setText(quote.getStrQuote());
        	
        	quotesView.addView(quoteView);
        }
    }
    
    public void addQuote(String strQuote) {
    	Quote quote = new Quote();
    	quote.setStrQuote(strQuote);
    	
    	quotesArray.add(quote);
    }
}