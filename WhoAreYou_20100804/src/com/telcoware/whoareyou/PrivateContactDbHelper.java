package com.telcoware.whoareyou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.telcoware.whoareyou.PrivateContactDb.PrivateContacts;

public class PrivateContactDbHelper extends SQLiteOpenHelper {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "PrivateContactDbHelper";
	
	private static final String DB_NAME = "mytest.db";
	private static final int DB_VERSION = 5;

	PrivateContactDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + PrivateContacts.TABLE_NAME + " (" 
				   + PrivateContacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				   + PrivateContacts.NUMBER + " TEXT,"
				   + PrivateContacts.NAME + " TEXT"
				   + ");");
		Util.log(logApplicationTag, logClassTag, "Private Contact DB Create, Table Name = " + PrivateContacts.TABLE_NAME);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE " + PrivateContacts.TABLE_NAME);
		this.onCreate(db);
		Util.log(logApplicationTag, logClassTag, "Private Contact DB Upgrade from oldVersion " + oldVersion + " to " + newVersion);
	}

	
}
