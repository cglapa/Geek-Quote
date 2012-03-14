package com.supinfo.geekquote;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.RatingBar;
import android.widget.TextView;

public class QuoteActivity extends Activity {
	private Quote quote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quoteactivity);
		
		Bundle extras = getIntent().getExtras();
		quote = (Quote) extras.getSerializable("quote");
		
		TextView quoteText = (TextView) findViewById(R.id.quoteText);
		TextView quoteDate = (TextView) findViewById(R.id.quoteDate);
		RatingBar quoteNote = (RatingBar) findViewById(R.id.quoteNote);
		
		quoteText.setText(quote.getStrQuote());
		String date = DateFormat.format("'The 'yyyy-MM-dd' at 'kk:mm", quote.getCreationDate()).toString();
		quoteDate.setText(date);
		quoteNote.setRating(quote.getRating());
	}

}
