package com.supinfo.geekquote;

import com.supinfo.geekquote.model.Quote;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuoteActivity extends Activity implements OnRatingBarChangeListener, OnClickListener, OnLongClickListener, android.content.DialogInterface.OnClickListener {
	private Quote quote;
	private EditText editQuote;
	private TextView quoteText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quoteactivity);
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		quote = (Quote) extras.getSerializable("quote");
		
		quoteText = (TextView) findViewById(R.id.quoteText);
		TextView quoteDate = (TextView) findViewById(R.id.quoteDate);
		RatingBar quoteNote = (RatingBar) findViewById(R.id.quoteNote);
		Button cancel = (Button) findViewById(R.id.quoteCancel);
		Button ok = (Button) findViewById(R.id.quoteSave);
		
		quoteText.setText(quote.getStrQuote());
		String date = DateFormat.format("'The 'yyyy-MM-dd' at 'kk:mm", quote.getCreationDate()).toString();
		quoteDate.setText(date);
		quoteNote.setRating(quote.getRating());
		
		quoteText.setOnLongClickListener(this);
		quoteNote.setOnRatingBarChangeListener(this);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
		}
		return super.onOptionsItemSelected(item);
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
	
	public void onClick(DialogInterface arg0, int arg1) {
		switch(arg1) {
		case AlertDialog.BUTTON_POSITIVE:
			quote.setStrQuote(editQuote.getText().toString());
			quoteText.setText(editQuote.getText());
		}
	}

	public boolean onLongClick(View v) {
		Resources res = getResources();
		
		editQuote = new EditText(this);
		editQuote.setSingleLine(false);
		editQuote.setText(quote.getStrQuote());
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(R.string.editquote);
		dialog.setView(editQuote);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, res.getText(R.string.cancel), this);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, res.getText(R.string.ok), this);
		dialog.show();
		return false;
	}
}
