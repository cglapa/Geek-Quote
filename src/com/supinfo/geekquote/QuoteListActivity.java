package com.supinfo.geekquote;

import java.util.ArrayList;
import java.util.Calendar;

import com.supinfo.geekquote.adapter.QuoteListAdapter;
import com.supinfo.geekquote.listener.QuoteListTextviewListener;
import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class QuoteListActivity extends Activity implements View.OnClickListener, TextWatcher {
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	private EditText quoteField;
	private Button quoteButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        QuoteListTextviewListener.setActivity(this);
        
        quoteButton = (Button) findViewById(R.id.quotebutton);
        quoteField = (EditText) findViewById(R.id.quotefield);
        ListView quotesView = (ListView) findViewById(R.id.quotesview);
        
        QuoteListAdapter quotesAdapter = new QuoteListAdapter(quotesArray,this);
        quotesView.setAdapter(quotesAdapter);
        
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
    	quote.setCreationDate(Calendar.getInstance().getTime());
    	
    	quotesArray.add(quote);
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