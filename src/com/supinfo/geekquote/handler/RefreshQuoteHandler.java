package com.supinfo.geekquote.handler;

import com.supinfo.geekquote.adapter.QuoteListAdapter;

import com.supinfo.geekquote.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class RefreshQuoteHandler extends Handler {
	private Context c;
	private QuoteListAdapter quotesAdapter;
	private ProgressDialog progress;
	
	public static final int STATUS_STARTED = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_ERROR_TIMEDOUT = 3;
	public static final int STATUS_ERROR_CONNREFUSED = 4;
	public static final int STATUS_ERROR_JSON = 5;
	public static final int STATUS_ERROR_UNKNOWN = 6;
	
	public RefreshQuoteHandler(Context c, QuoteListAdapter quotesAdapter) {
		this.c = c;
		this.quotesAdapter = quotesAdapter;
	}

	@Override
	public void handleMessage(Message msg) {
		int status = msg.arg1;
		Toast toast = null;
		switch(status) {
		case STATUS_STARTED:
			progress = new ProgressDialog(c);
			progress.setIndeterminate(true);
			progress.setMessage("Refreshing quotes");
			progress.setCancelable(false);
			progress.show();
			break;
		case STATUS_FINISHED:
			quotesAdapter.notifyDataSetChanged();
			toast = Toast.makeText(c, R.string.refresh_quote_finished, 1000);
			break;
		case STATUS_ERROR_TIMEDOUT:
			toast = Toast.makeText(c, R.string.refresh_quote_error_timedout, 1000);
			break;
		case STATUS_ERROR_CONNREFUSED:
			toast = Toast.makeText(c, R.string.refresh_quote_error_connrefused, 1000);
			break;
		case STATUS_ERROR_JSON:
			toast = Toast.makeText(c, R.string.refresh_quote_error_json, 1000);
			break;
		case STATUS_ERROR_UNKNOWN:
			toast = Toast.makeText(c, R.string.refresh_quote_error_unknown, 1000);
		}
		if(toast != null) {
			progress.dismiss();
			toast.show();
		}
		super.handleMessage(msg);
	}

}
