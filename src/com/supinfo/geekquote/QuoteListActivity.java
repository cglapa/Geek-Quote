package com.supinfo.geekquote;

import java.util.ArrayList;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteListActivity extends Activity {
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	private LinearLayout quotesView;
	private boolean isEven = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        quotesView = (LinearLayout) findViewById(R.id.quotesview);
        
        Resources res = getResources();
        String quotes[] = res.getStringArray(R.array.quotes);
        for(String quote : quotes) {
        	addQuote(quote);
        }
    }
    
    public void addQuote(String strQuote) {
    	Quote quote = new Quote();
    	quote.setStrQuote(strQuote);
    	
    	quotesArray.add(quote);
    	
    	TextView quoteView = new TextView(QuoteListActivity.this);
    	quoteView.setText(quote.getStrQuote());
    	
    	if(isEven)
    		quoteView.setBackgroundColor(Color.DKGRAY);
    	isEven = !isEven;
    	
    	quotesView.addView(quoteView);
    }
}