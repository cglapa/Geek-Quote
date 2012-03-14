package com.supinfo.geekquote.listener;

import com.supinfo.geekquote.QuoteActivity;
import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class QuoteListTextviewListener implements OnClickListener {
	private static Activity activity;
	private Quote quote;
	
	public static void setActivity(Activity a) {
		activity = a;
	}
	
	public QuoteListTextviewListener(Quote q) {
		quote = q;
	}

	public void onClick(View v) {
		Intent intent = new Intent(activity, QuoteActivity.class);
		intent.putExtra("quote", quote);
		activity.startActivity(intent);
	}

}
