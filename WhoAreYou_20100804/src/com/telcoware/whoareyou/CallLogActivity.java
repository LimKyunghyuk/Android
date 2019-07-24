package com.telcoware.whoareyou;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

public class CallLogActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "CallLogActivity";
	
	private static final int SHOW_MEMBERSHIPACTIVITY = Menu.FIRST + 1;
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	TableLayout callTable;
	TableRow callRowTemplate;
	LinearLayout callLinear1Template;
	TextView callNumberTextTemplate;
	ImageView callSpamLevelImageTemplate;
	TextView callSpamKeywordTextTemplate;
	TextView callDateTextTemplate;
	
	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_log);
		context = getApplicationContext();
		
		telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			public void onCallStateChanged(int callState, String inComingNumber) {               
            	switch (callState) {
        		case TelephonyManager.CALL_STATE_RINGING:
        			finish();
            	}  
    		}
		};
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    			
		// Get Views
		callTable = (TableLayout)findViewById(R.id.CallTable);
		callRowTemplate = (TableRow)findViewById(R.id.CallRow);
		callLinear1Template = (LinearLayout)findViewById(R.id.CallLinear1);
		callNumberTextTemplate = (TextView)findViewById(R.id.CallNumberText);	
		callSpamLevelImageTemplate = (ImageView)findViewById(R.id.CallSpamLevelImage);
		callSpamKeywordTextTemplate = (TextView)findViewById(R.id.CallSpamKeywordText);
		callDateTextTemplate = (TextView)findViewById(R.id.CallDateText);
		
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Check membership
		if (configPrefs.getBoolean("certification", false) == false) {
			Util.log(logApplicationTag, logClassTag, "this user hasn't a membership. servece is unavailable.");
			
			Intent intent = new Intent(this, MembershipActivity.class);
			startActivityForResult(intent, SHOW_MEMBERSHIPACTIVITY);
			
			return;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		
		// Check membership
		if (configPrefs.getBoolean("certification", false) == false) {
			Util.log(logApplicationTag, logClassTag, "this user hasn't a membership. servece is unavailable.");
			return;
		}

		// Create CallLog Db
		CallLogDbHelper dbHelper = new CallLogDbHelper(this.getApplicationContext());					
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(CallLogs.TABLE_NAME, 
										null, null, null, null, null, CallLogs.DEFAULT_SORT_ORDER);	
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			
			TableRow callRow = new TableRow(this);
			callRow.setLayoutParams(callRowTemplate.getLayoutParams());

			LinearLayout callLinear1 = new LinearLayout(this);			
			callLinear1.setLayoutParams(callLinear1Template.getLayoutParams());
			
			callLinear1.setBackgroundColor(Color.BLACK);	
			callLinear1.setOrientation(callLinear1Template.getOrientation());
			callLinear1.setTag(cursor.getInt(cursor.getColumnIndex(CallLogs._ID)));
			View callLinear1View = (View)callLinear1;
			callLinear1View.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Integer _id = (Integer)view.getTag();
					Util.log(logApplicationTag, logClassTag, "Seleted RowID : " + _id);
					
					// Create CallLog Db
					CallLogDbHelper dbHelper = new CallLogDbHelper(context.getApplicationContext());					
					SQLiteDatabase db = dbHelper.getWritableDatabase();
		            Cursor cursor = db.query(CallLogs.TABLE_NAME, null,
		            								"_id = ?", new String[] { _id.toString() }, null, null, null);
					cursor.moveToFirst();            
					String number = cursor.getString(cursor.getColumnIndex(CallLogs.NUMBER));
					int type = cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE));

					cursor.close();
					db.close();
					
					Intent intent = new Intent(CallLogActivity.this, ProgressActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra(getResources().getString(R.string.cmd), 
									getResources().getString(R.string.cmd_spam_search));
					intent.putExtra(getResources().getString(R.string.return_intent),
									getResources().getString(R.string.return_intent_call_log_detail));
					intent.putExtra(getResources().getString(R.string.param_rowid), _id);
					intent.putExtra(getResources().getString(R.string.param_number), number);
					intent.putExtra(getResources().getString(R.string.param_type), type);
					startActivity(intent);  
				}
				
			});
			
			callLinear1View.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						v.setBackgroundColor(Color.BLACK);
						break;
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						v.setBackgroundColor(Color.argb(80, 255, 165, 0));
						break;
					default:
						v.setBackgroundColor(Color.BLACK);
					}
					return false;
				}
			});
			
			
			RelativeLayout callRelative11 = new RelativeLayout(this);			
			callRelative11.setLayoutParams(findViewById(R.id.CallRelative11).getLayoutParams());
			
			TextView callNumberText = new TextView(this);			
			callNumberText.setLayoutParams(callNumberTextTemplate.getLayoutParams());
			callNumberText.setTextSize(callNumberTextTemplate.getTextSize());
			callNumberText.setText(cursor.getString(cursor.getColumnIndex(CallLogs.NUMBER)));	
			
			ImageView callTypeImage = new ImageView(this);
			callTypeImage.setLayoutParams(findViewById(R.id.CallTypeImage).getLayoutParams());			
			if (cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE)) == 0) {
				callTypeImage.setImageDrawable(getResources().getDrawable(R.drawable.callincoming));
			} 
			else if (cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE)) == 1) {
				callTypeImage.setImageDrawable(getResources().getDrawable(R.drawable.callmissed));
			} 
			else {
				callTypeImage.setImageDrawable(getResources().getDrawable(R.drawable.sms));
			}

			RelativeLayout callRelative12 = new RelativeLayout(this);
			callRelative12.setLayoutParams(findViewById(R.id.CallRelative12).getLayoutParams());
			LinearLayout callLinear121 = new LinearLayout(this);
			callLinear121.setLayoutParams(findViewById(R.id.CallLinear121).getLayoutParams());
			ImageView callSpamLevelImage = new ImageView(this);
			callSpamLevelImage.setLayoutParams(callSpamLevelImageTemplate.getLayoutParams());
			callSpamLevelImage.setImageDrawable(Util.getSpamLevelImage(context, cursor.getInt(cursor.getColumnIndex(CallLogs.SPAM_LEVEL))));
			TextView callSpamKeywordText = new TextView(this);
			callSpamKeywordText.setLayoutParams(callSpamKeywordTextTemplate.getLayoutParams());
			callSpamKeywordText.setTextSize(callSpamKeywordTextTemplate.getTextSize());
			callSpamKeywordText.setText(Util.getTopSpamKeywordHtml(cursor.getString(cursor.getColumnIndex(CallLogs.SPAM_KEYWORD))));
			LinearLayout callLinear122 = new LinearLayout(this);
			callLinear122.setLayoutParams(findViewById(R.id.CallLinear122).getLayoutParams());			
			TextView callDateText = new TextView(this);
			callDateText.setLayoutParams(callDateTextTemplate.getLayoutParams());
			callDateText.setTextSize(callDateTextTemplate.getTextSize());
    		SimpleDateFormat formatter = new SimpleDateFormat ( "MM/dd HH:mm", Locale.KOREA );
    		Date smsDate = new Date(cursor.getLong(cursor.getColumnIndex(CallLogs.DATE)));
    		String dateStr = formatter.format(smsDate);
    		callDateText.setText(dateStr);
    		/*
			callDateText.setText(DateUtils.getRelativeTimeSpanString(
								 cursor.getLong(cursor.getColumnIndex(CallLogs.DATE)),
									System.currentTimeMillis(), 0, DateUtils.FORMAT_ABBREV_RELATIVE));			
			*/
    		
			//Add Views
			callRelative11.addView(callNumberText);
			callRelative11.addView(callTypeImage);

			callLinear121.addView(callSpamLevelImage);
			callLinear121.addView(callSpamKeywordText);
			callLinear122.addView(callDateText);
					
			callRelative12.addView(callLinear121);
			callRelative12.addView(callLinear122);
			
			callLinear1.addView(callRelative11);
			callLinear1.addView(callRelative12);
			
			callRow.addView(callLinear1);
			
			callTable.addView(callRow);
			
			cursor.moveToNext();
		}
		
		cursor.close();
		db.close();
		
		callTable.removeView(callRowTemplate);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		callTable.removeAllViews();
		callTable.addView(callRowTemplate);		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
	
	private void CallLogInit() {	
		Cursor cursor;
		ContentValues values;
		CallLogDbHelper dbHelper;
		SQLiteDatabase db;
		long rowId;
		int count;
		
		// Get Call Log Db
		dbHelper = new CallLogDbHelper(this.getApplicationContext());					
		db = dbHelper.getWritableDatabase();
		
		// Insert Call
		cursor = managedQuery(CallLog.Calls.CONTENT_URI, 
				              new String[] { android.provider.CallLog.Calls.NUMBER, CallLog.Calls.DATE },
				              null, null, null);

		cursor.moveToFirst();
		count = 0;
		while (cursor.isAfterLast() == false) {
			String inComingNumber = Util.getPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
			long inComingDuration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
			
			//Search InComingNumber in Contract
			if (Util.ContactExist(context, inComingNumber) >= 0) {
				Util.log(logApplicationTag, logClassTag, inComingNumber + "is in Contracts");
				cursor.moveToNext();
				continue;
			}
			
    		// Basic Call Log DB Insert
    		values = new ContentValues();
    		values.put(CallLogs.NUMBER, inComingNumber);
    		values.put(CallLogs.NAME, getResources().getString(R.string.unknown));
    		values.put(CallLogs.SPAM_LEVEL, -1); //Unknown
    		values.put(CallLogs.SPAM_KEYWORD, getResources().getString(R.string.unknown));
    		values.put(CallLogs.SPAM_REGI, 0); //UnRegi
    		values.put(CallLogs.DATE, inComingDuration);
    		values.put(CallLogs.TYPE, 0); //Incoming
    		rowId = -1;
    		rowId = db.insert(CallLogs.TABLE_NAME, null, values);
    		if (rowId < 0) {
    			Util.log(logApplicationTag, logClassTag, "DB Insert Fail");
    			cursor.moveToNext();
    			continue;
    		}
    		Util.log(logApplicationTag, logClassTag, "Insert Call Log, rowId = " + rowId + ", inComingNumber = " + inComingNumber);
    		count++;
			if (count >= 5) {
				break;
			}
			
			cursor.moveToNext();
		}
		cursor.close();
		
		// Insert SMS
        cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null,null);

        cursor.moveToFirst();
        count = 0;
        while (cursor.isAfterLast() == false) {
    		String inboxAddress = Util.getPhoneNumber(cursor.getString(cursor.getColumnIndex("address")));
			String inboxContent = cursor.getString(cursor.getColumnIndex("body"));
			long inboxTime = cursor.getLong(cursor.getColumnIndex("date"));
			Util.log(logApplicationTag, logClassTag, "inboxAddress = " + inboxAddress);

    		//Search inboxAddress in Contract
    		if (Util.ContactExist(context, inboxAddress) >= 0) {
    			Util.log(logApplicationTag, logClassTag, inboxAddress + "is in Contracts");
    			cursor.moveToNext();
    			continue;
    		}
    		
    	    // Basic SMS Log DB Insert
    	    values = new ContentValues();
    	    values.put(CallLogs.NUMBER, inboxAddress);
    	    values.put(CallLogs.NAME, getResources().getString(R.string.unknown));
    	    values.put(CallLogs.SPAM_LEVEL, -1); //Unknown
    	    values.put(CallLogs.SPAM_KEYWORD, getResources().getString(R.string.unknown));
    	    values.put(CallLogs.SPAM_REGI, 0); //UnRegi
    	    values.put(CallLogs.DATE, inboxTime);
    	    values.put(CallLogs.TYPE, 2); //SMS
    	    values.put(CallLogs.CONTENT, inboxContent);
    	    rowId = -1;		
    	    rowId = db.insert(CallLogs.TABLE_NAME, null, values);
    	    if (rowId < 0) {
    	    	Util.log(logApplicationTag, logClassTag, "DB Insert Fail");
    			cursor.moveToNext();
    			continue;
    	    }
    	    Util.log(logApplicationTag, logClassTag, "Insert SMS Log, rowId = " + rowId + ", inboxAddress = " + inboxAddress);
    	    count++;		
			if (count >= 5) {
				break;
			}
			
        	cursor.moveToNext();
        }
        cursor.close();
        db.close();
	
		Intent intent = new Intent(CallLogActivity.this, MainActivity.class);
		startActivity(intent);  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Util.log(logApplicationTag, logClassTag, "requestCode : " + requestCode + ", resultCode : " + resultCode);
			
		switch(requestCode) {
		case (SHOW_MEMBERSHIPACTIVITY):
			if (data != null)
			{
				int nResult = data.getIntExtra("result", 0);
				
				Util.log(logApplicationTag, logClassTag, "membership result : " + nResult);
				
				if (nResult == 1)
				{
					prefsEditor.putBoolean("certification", true);
					prefsEditor.commit();
					
					new AlertDialog.Builder(CallLogActivity.this)
					.setMessage(R.string.member_ok)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							CallLogInit();
						}
						
					})
					.show();
				}
				else if (nResult == 0)
				{
					prefsEditor.putBoolean("certification", true);
					prefsEditor.commit();
					
					new AlertDialog.Builder(CallLogActivity.this)
					.setMessage(R.string.member_already_exist)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							CallLogInit();
						}
						
					})
					.show();
				}
				else //if (nResult == -1)
				{
					new AlertDialog.Builder(CallLogActivity.this)
					.setMessage(R.string.member_na)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							finish();
						}
						
					})
					.show();
				}
			}
			else
			{
				Util.log(logApplicationTag, logClassTag, "Intent data is null");
				
				new AlertDialog.Builder(CallLogActivity.this)
				.setMessage(R.string.member_notok)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						finish();
					}
					
				})
				.show();
			}
			break;
		}
	}
}
