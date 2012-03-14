package com.supinfo.geekquote.adapter;

import java.util.List;

import com.supinfo.geekquote.listener.QuoteListTextviewListener;
import com.supinfo.geekquote.model.Quote;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuoteListAdapter extends BaseAdapter {
	private boolean isEven = false;
	private List<Quote> quotes;
	private Context context;
	
	public QuoteListAdapter(List<Quote> quotes, Context context) {
		this.quotes = quotes;
		this.context = context;
	}

	public int getCount() {
		return quotes.size();
	}

	public Quote getItem(int position) {
		return quotes.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text = new TextView(context);
		Quote item = getItem(position);
		
		QuoteListTextviewListener listener = new QuoteListTextviewListener(position, item);
		
        text.setText(item.getStrQuote());
        text.setOnClickListener(listener);
        
        if(isEven)
        	text.setBackgroundColor(Color.DKGRAY);
        isEven = !isEven;

        return text;
	}

}
