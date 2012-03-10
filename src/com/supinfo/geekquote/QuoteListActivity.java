package com.supinfo.geekquote;

import java.util.ArrayList;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteListActivity extends Activity implements View.OnClickListener, TextWatcher {
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	private LinearLayout quotesView;
	private EditText quoteField;
	private Button quoteButton;
	private boolean isEven = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        quoteButton = (Button) findViewById(R.id.quotebutton);
        quoteField = (EditText) findViewById(R.id.quotefield);
        quotesView = (LinearLayout) findViewById(R.id.quotesview);
        
        quoteButton.setOnClickListener(this);
        quoteField.addTextChangedListener(this);
        
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

	public void onClick(View v) {
		addQuote(quoteField.getText().toString());
		quoteField.setText("");
	}

	public void afterTextChanged(Editable s) {}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		boolean clickable = !("".equals(quoteField.getText().toString()));
		quoteButton.setEnabled(clickable);
	}
}