package com.supinfo.geekquote;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuoteActivity extends Activity implements OnRatingBarChangeListener, OnClickListener {
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
		Button cancel = (Button) findViewById(R.id.quoteCancel);
		Button ok = (Button) findViewById(R.id.quoteSave);
		
		quoteText.setText(quote.getStrQuote());
		String date = DateFormat.format("'The 'yyyy-MM-dd' at 'kk:mm", quote.getCreationDate()).toString();
		quoteDate.setText(date);
		quoteNote.setRating(quote.getRating());
		
		quoteNote.setOnRatingBarChangeListener(this);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		quote.setRating((int) rating);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.quoteSave:
			getIntent().putExtra("quote", quote);
			setResult(RESULT_OK, getIntent());
			break;
		case R.id.quoteCancel:
			setResult(RESULT_CANCELED);
		}
		finish();
	}
}
