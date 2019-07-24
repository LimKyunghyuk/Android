package com.telcoware.whoareyou;

import java.io.IOException;
import java.net.URLEncoder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

public class ProgressActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "ProgressActivity";
	
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	
	SharedPreferences devConfigPrefs;
	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;
	
	Integer rowId;
	String command;
	String returnResult;
	String returnIntent;

	Bundle extras;
	String callNumber;
	String callName;
	String callKeyword;
	int callType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process);
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
	}

	private void startReturnIntentActivity() {
		Intent intent;
		if (returnIntent.compareTo(getResources().getString(R.string.return_intent_call_log_detail)) == 0) {
			intent = new Intent(ProgressActivity.this, CallLogDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra(getResources().getString(R.string.param_rowid), rowId);
			intent.putExtra(getResources().getString(R.string.cmd),
					 		getResources().getString(R.string.cmd_return));
			intent.putExtra(getResources().getString(R.string.return_result),
							returnResult);
			Util.log(logApplicationTag, logClassTag, "startReturnIntentActivity:: returnResult=" + returnResult);
			startActivity(intent);				
		}
		else {
			intent = new Intent(ProgressActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// Get DevPreferences
		try {
			devConfigPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		// Get Preferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		extras = this.getIntent().getExtras();
		command = extras.getString(getResources().getString(R.string.cmd));
		returnIntent = extras.getString(getResources().getString(R.string.return_intent));
		rowId = extras.getInt(getResources().getString(R.string.param_rowid));
		callNumber = extras.getString(getResources().getString(R.string.param_number));
		callType = extras.getInt(getResources().getString(R.string.param_type));
		returnResult = getResources().getString(R.string.ok);
		
		Util.log(logApplicationTag, logClassTag, "Command=" + command + ", ReturnIntent=" + returnIntent + 
				      ", rowId=" + rowId + ", callNumber=" + callNumber + ", callType=" + callType);

		// check Network
		int ret;
		if (Util.isAvailableNetwork(context, configPrefs.getInt("network", 0)) >= 0) {		
			if (command.compareTo(getResources().getString(R.string.cmd_spam_search)) == 0) {
				ret = searchSpamAndUpdateDB();
			}
			else if (command.compareTo(getResources().getString(R.string.cmd_spam_level_up)) == 0) {
				callKeyword = extras.getString(getResources().getString(R.string.param_keyword));
				ret = regiSpamAndUpdateDB(callKeyword);
				if (ret >= 0) {
					ret = searchSpamAndUpdateDB();
				}
			}
			else if (command.compareTo(getResources().getString(R.string.cmd_spam_level_down)) == 0) {		
				ret = unRegiSpamAndUpdateDB();
				if (ret >= 0) {
					ret = searchSpamAndUpdateDB();
				}
			}
			else {
				Util.log(logApplicationTag, logClassTag, "UnKnown Command " + command);
				ret = -1;
			}			
		}
		else {
			ret = -1;
		}
		
		if (ret < 0) {
			returnResult = getResources().getString(R.string.failed_spam_search) + "\n" + 
						   getResources().getString(R.string.check_network);
		}
		Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
         // Do Something
         public void run() {     
     		startReturnIntentActivity();
         }
        }, 100);        
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
	
	private int searchSpamAndUpdateDB() {
    	String result, keywordList = " ", regiKeywordList = " ";
    	String keyword1, keyword2, keyword3;
    	String keyword1Grade, keyword2Grade, keyword3Grade;
    	String spamGrade;
    	String firmName;
    	
    	try {
    		String feed;
    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
    		{	
    			feed = getString(R.string.spam_feed);
    		} else
    		{
    			feed = getString(R.string.spam_feed_outter);
    		}
    		String spamFeed = feed + callNumber + 
			"&mode=search" + 
			"&key=" + Util.convertKey(callNumber) + 
			"&option=keyword_list";	
    		if (callType != 2) {
    			spamFeed += "&type=phone";
    		}
    		else {
    			spamFeed += "&type=sms";
    		}
    		Util.log(logApplicationTag, logClassTag, "SpamFeed: " + spamFeed);
    		
    		Document dom = Util.queryHTTP(spamFeed, 4000);
    		if (dom != null) {
				Element docEle = dom.getDocumentElement();
				Element entry = (Element)docEle.getElementsByTagName("spam_call").item(0);		
				result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
				spamGrade = entry.getElementsByTagName("spam_grade").item(0).getFirstChild().getNodeValue();
				firmName = entry.getElementsByTagName("firm_name").item(0).getFirstChild().getNodeValue();

				// Config Update
				Element keyWordEntry = (Element) entry.getElementsByTagName("keyword_list").item(0);
				regiKeywordList = keyWordEntry.getFirstChild().getNodeValue();
    			prefsEditor.putString("regi_keyword_list", regiKeywordList);
    			prefsEditor.commit();
    			Util.log(logApplicationTag, logClassTag, "config Update regi_keyword_list=" + regiKeywordList);
				
				if (!result.toString().equals("ok")) {
					Util.log(logApplicationTag, logClassTag, "HTTP Result Fail: " + result.toString());					
				} else {
					keyWordEntry = (Element) entry.getElementsByTagName("keyword1").item(0);
					keyword1 = keyWordEntry.getFirstChild().getNodeValue();
					keyword1Grade = keyWordEntry.getAttribute("grade");
	
					keyWordEntry = (Element) entry.getElementsByTagName("keyword2").item(0);
					keyword2 = keyWordEntry.getFirstChild().getNodeValue();
					keyword2Grade = keyWordEntry.getAttribute("grade");
					
					keyWordEntry = (Element) entry.getElementsByTagName("keyword3").item(0);
					keyword3 = keyWordEntry.getFirstChild().getNodeValue();
					keyword3Grade = keyWordEntry.getAttribute("grade");
					
					if (!keyword1.toString().equals(" ")) {
						keywordList = keyword1 + ":" + keyword1Grade;
						if (!keyword2.toString().equals(" ")) {
							keywordList = keywordList + ","  +  keyword2 + ":" + keyword2Grade;
						}
						if (!keyword3.toString().equals(" ")) {
							keywordList = keywordList + "," + keyword3 + ":" + keyword3Grade;
						}
					}
				}
				// DB Update
		    	ContentValues contentValues = new ContentValues();			    	
		    	contentValues.put(CallLogs.NAME, firmName.toString());
        		contentValues.put(CallLogs.SPAM_LEVEL, Integer.parseInt(spamGrade));
        		contentValues.put(CallLogs.SPAM_KEYWORD, keywordList.toString());
        		
				CallLogDbHelper dbHelper = new CallLogDbHelper(getApplicationContext());					
				SQLiteDatabase db = dbHelper.getWritableDatabase();
        		db.update(CallLogs.TABLE_NAME, contentValues, "_id = ?", new String[] { rowId.toString() });
        		db.close();
        		Util.log(logApplicationTag, logClassTag, "DB Udate rowId: " + rowId);
    			
        		return 0;
    		}
    		else {
				return -1;

    		}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return -1;
	}
	
	private int regiSpamAndUpdateDB(String spamKeyword) {  	
    	try {    	
    		String feed;
    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
    		{	
    			feed = getString(R.string.spam_regi_feed);
    		} else
    		{
    			feed = getString(R.string.spam_regi_feed_outter);
    		}
    		
    		TelephonyManager telephony = (TelephonyManager)getSystemService(Activity.TELEPHONY_SERVICE);
    		
    		String spamFeed = feed + callNumber + 
    								"&key=" + Util.convertKey(callNumber) + 
    								"&spam_keyword=" + URLEncoder.encode(spamKeyword, "UTF-8")+ 
    								"&user_id=" + telephony.getLine1Number();
    		if (callType != 2) {
    			spamFeed += "&type=phone";
    		}
    		else {
    			spamFeed += "&type=sms";
    		}
    		Util.log(logApplicationTag, logClassTag, "SpamFeed: " + spamFeed);
    		
    		Document dom = Util.queryHTTP(spamFeed, 4000);
    		if (dom != null) {
				Element docEle = dom.getDocumentElement();
				Element entry = (Element)docEle.getElementsByTagName("spam_regi").item(0);
				String result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();			
				if (!result.toString().equals("ok")) {
					Util.log(logApplicationTag, logClassTag, "regiSpamAndUpdateDB:: HTTP Result Fail: " + result.toString());
					return 0;
				}
				Util.log(logApplicationTag, logClassTag, "Spam Level Up, Keyword=" + spamKeyword);
				
		    	ContentValues contentValues = new ContentValues();		    	
		    	contentValues.put(CallLogs.SPAM_REGI, 1);
		    	contentValues.put(CallLogs.SPAM_REGI_KEYWORD, spamKeyword);
		    	
				CallLogDbHelper dbHelper = new CallLogDbHelper(getApplicationContext());					
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.update(CallLogs.TABLE_NAME, contentValues, "_id = ?", new String[] { new Long(rowId).toString() });
				db.close();
				Util.log(logApplicationTag, logClassTag, "Update SpamRegi: 1 On DB");
				return 0;
    		}
    		else {
    			return -1;
    		}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		return -1;
	}
	
	private int unRegiSpamAndUpdateDB() {  	
    	try {
    		String feed;
    		
    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
    		{	
    			feed = getString(R.string.spam_unregi_feed);
    		} else
    		{
    			feed = getString(R.string.spam_unregi_feed_outter);
    		}
    		
			CallLogDbHelper dbHelper = new CallLogDbHelper(getApplicationContext());					
			SQLiteDatabase db = dbHelper.getWritableDatabase();
    		// Query: SELECT spam_regi_keyword FROM call_logs WHERE _id=rowId
    		Cursor cursor = db.query(CallLogs.TABLE_NAME, new String[] { CallLogs.SPAM_REGI_KEYWORD }, 
    				                 "_id = ?", new String[] { new Long(rowId).toString() }, null, null, null);
    		cursor.moveToFirst();
    		String spamKeyword = cursor.getString(cursor.getColumnIndex(CallLogs.SPAM_REGI_KEYWORD));
    		cursor.close();
    		
    		TelephonyManager telephony = (TelephonyManager)getSystemService(Activity.TELEPHONY_SERVICE);
    		
    		String spamFeed = feed + callNumber + 
    							"&key=" + Util.convertKey(callNumber)+ 
								"&spam_keyword=" + URLEncoder.encode(spamKeyword, "UTF-8")+
								"&user_id=" + telephony.getLine1Number();
    		if (callType != 2) {
    			spamFeed += "&type=phone";
    		}
    		else {
    			spamFeed += "&type=sms";
    		}
    		Util.log(logApplicationTag, logClassTag, "SpamFeed: " + spamFeed);
    		
    		Document dom = Util.queryHTTP(spamFeed, 4000);
    		if (dom != null) {
				Element docEle = dom.getDocumentElement();
				Element entry = (Element)docEle.getElementsByTagName("spam_unregi").item(0);				
				String result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
				if (!result.toString().equals("ok")) {
					Util.log(logApplicationTag, logClassTag, "HTTP Result Fail: " + result.toString());
					db.close();
					return -1;
				}				
		    	ContentValues contentValues = new ContentValues();		    	
		    	contentValues.put(CallLogs.SPAM_REGI, 0); 
				db.update(CallLogs.TABLE_NAME, contentValues, "_id = ?", new String[] { new Long(rowId).toString() });
				db.close();
				Util.log(logApplicationTag, logClassTag, "Update SpamRegi: 0 On DB");
				return 0;
    		}
    		else {
    			db.close();
    			return -1;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		return -1;
	}
}
