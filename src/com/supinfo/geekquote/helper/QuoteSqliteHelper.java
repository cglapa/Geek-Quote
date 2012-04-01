package com.supinfo.geekquote.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.supinfo.geekquote.model.Quote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

public class QuoteSqliteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "geekquote.db";
	private static final int DATABASE_VERSION = 2;
	public static final String TABLE_NAME = "quotes";
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + " ( " +
			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"server_id INTEGER UNIQUE, " +
			"quote TEXT NOT NULL, " +
			"rating INTEGER NOT NULL, " +
			"creation_date TEXT NOT NULL);";

	public QuoteSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public void insertQuoteRaw(Quote q, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("server_id", q.getServerId());
		values.put("quote", q.getStrQuote());
		values.put("rating", q.getRating());
		values.put("creation_date", DateFormat.format("yyyy-MM-dd kk:mm", q.getCreationDate()).toString());
		db.insert(TABLE_NAME, null, values);
	}
	
	public long insertQuote(Quote q) {
		SQLiteDatabase db = getWritableDatabase();
		long id = insertQuote(q, db);
		db.close();
		return id;
	}

	public long insertQuote(Quote q, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("quote", q.getStrQuote());
		values.put("creation_date", DateFormat.format("yyyy-MM-dd kk:mm", q.getCreationDate()).toString());
		return db.insert(TABLE_NAME, null, values);
	}
	
	public void updateQuote(Quote q) {
		SQLiteDatabase db = getWritableDatabase();
		updateQuote(q, db);
		db.close();
	}

	public void updateQuote(Quote q, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("quote", q.getStrQuote());
		values.put("rating", q.getRating());
		String where[] = { Long.toString(q.getId()) };
		db.update(TABLE_NAME, values, "id=?", where);
	}
	
	public Quote formatQuoteFromDbResult(Cursor r) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Quote q = new Quote();
		try {
			q.setId(r.getLong(0));
			q.setServerId(r.getLong(1));
    		q.setStrQuote(r.getString(2));
    		q.setRating(r.getInt(3));
    		try {
				q.setCreationDate(dateFormatter.parse(r.getString(4)));
			} catch (ParseException e) {
				e.printStackTrace();
				q.setCreationDate(Calendar.getInstance().getTime());
			}
		} catch(CursorIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return q;
	}
}
