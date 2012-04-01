package com.supinfo.geekquote;

import java.util.ArrayList;
import java.util.Calendar;

import com.supinfo.geekquote.REST.RefreshQuoteREST;
import com.supinfo.geekquote.adapter.QuoteListAdapter;
import com.supinfo.geekquote.handler.RefreshQuoteHandler;
import com.supinfo.geekquote.helper.QuoteSqliteHelper;
import com.supinfo.geekquote.listener.QuoteListTextviewListener;
import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class QuoteListActivity extends Activity implements View.OnClickListener, TextWatcher {
	public static final int QUOTE_ACTIVITY_CODE = 1;
	
	private ArrayList<Quote> quotesArray = new ArrayList<Quote>();
	private EditText quoteField;
	private Button quoteButton;
	private QuoteListAdapter quotesAdapter;
	private QuoteSqliteHelper sql;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.refresh_button:
			RefreshQuoteHandler handler = new RefreshQuoteHandler(this, quotesAdapter);
			RefreshQuoteREST rq = new RefreshQuoteREST(handler, sql, quotesArray);
			Thread t = new Thread(rq);
			t.start();
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        QuoteListTextviewListener.setActivity(this);
        
        quoteButton = (Button) findViewById(R.id.quotebutton);
        quoteField = (EditText) findViewById(R.id.quotefield);
        ListView quotesView = (ListView) findViewById(R.id.quotesview);
        
        quotesAdapter = new QuoteListAdapter(quotesArray,this);
        quotesView.setAdapter(quotesAdapter);
        
        quoteButton.setOnClickListener(this);
        quoteField.addTextChangedListener(this);
        
        sql = new QuoteSqliteHelper(QuoteListActivity.this);
        SQLiteDatabase db = sql.getWritableDatabase();
        Cursor results = db.query(QuoteSqliteHelper.TABLE_NAME, null, null, null, null, null, "id");
        
        if(results.getCount() > 0) {
        	Quote q;
        	results.moveToFirst();
        	while(!results.isAfterLast()) {
        		q = sql.formatQuoteFromDbResult(results);
        		quotesArray.add(q);
        		results.moveToNext();
        	}
        	results.close();
        } else {
	        Resources res = getResources();
	        String quotes[] = res.getStringArray(R.array.quotes);
	        for(String quote : quotes) {
	        	addQuote(quote, db);
	        }
        }
        db.close();
    }

	@Override
	protected void onStop() {
		sql.close();
		super.onStop();
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		case QUOTE_ACTIVITY_CODE:
				if(resultCode == RESULT_OK) {
						Bundle b = data.getExtras();
						Quote q = (Quote) b.getSerializable("quote");
						quotesArray.set(b.getInt("id"), q);
						quotesAdapter.notifyDataSetChanged();
						sql.updateQuote(q);
				}
				break;
		}	
    }
    
    public void addQuote(String strQuote, SQLiteDatabase db) {
    	Quote quote = new Quote();
    	quote.setStrQuote(strQuote);
    	quote.setCreationDate(Calendar.getInstance().getTime());
    	
		quote.setId(sql.insertQuote(quote, db));
    	
    	quotesArray.add(quote);
    }

	public void onClick(View v) {
		SQLiteDatabase db = sql.getWritableDatabase();
		addQuote(quoteField.getText().toString(), db);
		db.close();
		quoteField.setText("");
		quotesAdapter.notifyDataSetChanged();
	}

	public void afterTextChanged(Editable s) {}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		boolean clickable = !("".equals(quoteField.getText().toString()));
		quoteButton.setEnabled(clickable);
	}
}