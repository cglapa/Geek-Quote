package com.supinfo.geekquote.listener;

import com.supinfo.geekquote.QuoteActivity;
import com.supinfo.geekquote.QuoteListActivity;
import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class QuoteListTextviewListener implements OnClickListener {
	private int id;
	private static Activity activity;
	private Quote quote;
	
	public static void setActivity(Activity a) {
		activity = a;
	}
	
	public QuoteListTextviewListener(int id, Quote q) {
		this.id = id;
		quote = q;
	}

	public void onClick(View v) {
		Intent intent = new Intent(activity, QuoteActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("quote", quote);
		activity.startActivityForResult(intent, QuoteListActivity.QUOTE_ACTIVITY_CODE);
	}

}
