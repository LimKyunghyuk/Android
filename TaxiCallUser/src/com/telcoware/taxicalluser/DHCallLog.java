package com.telcoware.taxicalluser;

import android.content.*;
import android.database.sqlite.*;
import android.provider.*;

public class DHCallLog extends SQLiteOpenHelper implements BaseColumns
{
	private static final String	DB_NAME		= "TaxiCallLog.db";
	public static final String	TABLE_NAME	= "call_log";
	private static final int	DB_VER		= 1;
	
	public static final String DATE		= "call_date";
	public static final String TIME		= "call_time";
	public static final String TAXINUM	= "taxi_number";
	public static final String PHONENUM	= "taxi_phone";
	public static final String LICENSE	= "license_number";
	
	
	public DHCallLog(Context context)
	{
		super(context, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " INTEGER PRIMARY KEY, "
				+ DATE + " TEXT, "
				+ TIME + " TEXT, "
				+ TAXINUM + " TEXT, "
				+ PHONENUM + " TEXT, "
				+ LICENSE + " TEXT"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
}