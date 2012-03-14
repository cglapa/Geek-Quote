package com.supinfo.geekquote.listener;

import com.supinfo.geekquote.QuoteActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class QuoteListTextviewListener implements OnClickListener {
	private static Activity activity;
	
	public static void setActivity(Activity a) {
		activity = a;
	}

	public void onClick(View v) {
		Intent intent = new Intent(activity, QuoteActivity.class);
		activity.startActivity(intent);
	}

}
