package com.telcoware.whoareyou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

public class CallLogDbHelper extends SQLiteOpenHelper {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "CallLogDbHelper";
	
	private static final String DB_NAME = "call_logs.db";
	private static final int DB_VERSION = 5;

	CallLogDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + CallLogs.TABLE_NAME + " (" 
				   + CallLogs._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				   + CallLogs.NUMBER + " TEXT,"
				   + CallLogs.NAME + " TEXT,"
				   + CallLogs.SPAM_LEVEL + " INTEGER,"
				   + CallLogs.SPAM_KEYWORD + " TEXT,"
				   + CallLogs.SPAM_REGI + " INTEGER,"
				   + CallLogs.SPAM_REGI_KEYWORD + " TEXT,"				   
				   + CallLogs.DATE + " INTEGER,"
				   + CallLogs.TYPE + " INTEGER,"
				   + CallLogs.CONTENT + " TEXT"
				   + ");");
		Util.log(logApplicationTag, logClassTag, "Call Log DB Create, Table Name = " + CallLogs.TABLE_NAME);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE " + CallLogs.TABLE_NAME);
		this.onCreate(db);
		Util.log(logApplicationTag, logClassTag, "Call Log DB Upgrade from oldVersion " + oldVersion + " to " + newVersion);
	}
}
