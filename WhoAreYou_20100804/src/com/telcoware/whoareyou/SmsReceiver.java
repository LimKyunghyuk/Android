package com.telcoware.whoareyou;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

public class SmsReceiver extends BroadcastReceiver {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "SmsReceiver";
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
	
	Context context;
	Handler mHandler;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
			
		if (intent.getAction().equals(ACTION) == false) {
			return ;
		}
		
    	Bundle bundle = intent.getExtras();
    	if (bundle == null) {
    		return ;
    	}
    	
    	Object[] pdusObj = (Object[])bundle.get("pdus");
    	if (pdusObj == null) {
    		return ;
    	}
    	
    	this.context = context;

		SharedPreferences configPrefs = context.getSharedPreferences(context.getResources().getString(R.string.config_preferences), 0);
		
		//Service Available Check
   		if (configPrefs.getBoolean("service", false) == false) {
   			Util.log(logApplicationTag, logClassTag, "service is not available");
   			return;
   		}
    		
   		//Certification Available Check
   		if (configPrefs.getBoolean("certification", false) == false) {
   			Util.log(logApplicationTag, logClassTag, "certification is not available");
   			return;
   		}
   		
    	CallLogDbHelper dbHelper = new CallLogDbHelper(context.getApplicationContext());					
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	
   		int smsMessageLength = pdusObj.length;
   		SmsMessage[] smsMessages = new SmsMessage[smsMessageLength];
   		int insertNum = 0;
    	for (int i = 0; i < smsMessageLength; i++) {
        	smsMessages[i] = SmsMessage.createFromPdu((byte[])pdusObj[i]);
        	
        	String phoneNum = Util.getPhoneNumber(smsMessages[i].getOriginatingAddress());        	
        	String content = smsMessages[i].getMessageBody();
        	long date = smsMessages[i].getTimestampMillis();
        	
        	Util.log(logApplicationTag, logClassTag, "RECEIVE SMS pduAddress=" + phoneNum + ", pduTime=" + date);
        	
       		//Search inboxAddress in Contract
       		if (Util.ContactExist(context, phoneNum) >= 0) {
       			Util.log(logApplicationTag, logClassTag, phoneNum + " is in Contracts");
       			continue;
       		}
       		
    		ContentValues values = new ContentValues();
    		values.put(CallLogs.NUMBER, phoneNum);
    		values.put(CallLogs.NAME, context.getResources().getString(R.string.unknown));
    		values.put(CallLogs.SPAM_LEVEL, -1); //Unknown
    		values.put(CallLogs.SPAM_KEYWORD, context.getResources().getString(R.string.unknown));
    		values.put(CallLogs.SPAM_REGI, 0); //UnRegi
    		values.put(CallLogs.DATE, date);
    		values.put(CallLogs.TYPE, 2); //SMS
    		values.put(CallLogs.CONTENT, content);
    		
    		long rowId = -1;
    		rowId = db.insert(CallLogs.TABLE_NAME, null, values);
    		if (rowId >= 0) {
    			insertNum++;
    			Util.log(logApplicationTag, logClassTag, "Insert SMS" + ", insertNum = " + insertNum + ", rowId = " + rowId);
    		}
    	}
		db.close();
		
		searchSpamAndUpdateDB(insertNum);
	}
	
	private int searchSpamAndUpdateDB(int msgNum) {
		if (msgNum <= 0) {
			return 0;
		}
		
    	SharedPreferences configPrefs;
    	SharedPreferences devConfigPrefs;
		configPrefs = context.getSharedPreferences(context.getResources().getString(R.string.config_preferences), 0);
		devConfigPrefs = context.getSharedPreferences(context.getResources().getString(R.string.dev_config_preferences), 0);
		
		int smsFilterLevel = configPrefs.getInt("sms_filter_level", 2);
		
   		//Check Network Status          		
   		if (Util.isAvailableNetwork(context, configPrefs.getInt("network", 0)) < 0) {
   			Util.log(logApplicationTag, logClassTag, "network is not available");
   			return -1;
   		}

    	CallLogDbHelper dbHelper = new CallLogDbHelper(context.getApplicationContext());					
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(CallLogs.TABLE_NAME, 
				                 null, null, null, null, null, CallLogs.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int total = msgNum;
		int count = 0;
		while (cursor.isAfterLast() == false) {
			if (cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE)) != 2) {
				cursor.moveToNext();	
				continue;
			}
	    	
			count++;
			if (count > total) {
				break;
			}
			
	    	String inboxAddress = cursor.getString(cursor.getColumnIndex(CallLogs.NUMBER));
	    	String inboxContent = cursor.getString(cursor.getColumnIndex(CallLogs.CONTENT));
	    	Long inboxTime = cursor.getLong(cursor.getColumnIndex(CallLogs.DATE));
	    	
	    	String result, keywordList = " ";
	    	String keyword1, keyword2, keyword3;
	    	String keyword1Grade, keyword2Grade, keyword3Grade;
	    	String spamGrade;
	    	String firmName;
	
	    	try {
	    		String feed;
	    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
	    		{	
	    			feed = context.getString(R.string.spam_feed);
	    		} else
	    		{
	    			feed = context.getString(R.string.spam_feed_outter);
	    		}
	    		  		
	    		String spamFeed = feed + inboxAddress + 
				"&key=" + Util.convertKey(inboxAddress) +
				"&type=sms";	  		
	    		Util.log(logApplicationTag, logClassTag, "SpamPeed = " + spamFeed);
	    		
	    		Document dom = Util.queryHTTP(spamFeed, 4000);
	    		if (dom == null) {
	    			Util.log(logApplicationTag, logClassTag, "XML Document is NULL");
	    			cursor.moveToNext();	
					continue;
	    		}
	    		
				Element docEle = dom.getDocumentElement();
				Element entry = (Element)docEle.getElementsByTagName("spam_call").item(0);		
				result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
				spamGrade = entry.getElementsByTagName("spam_grade").item(0).getFirstChild().getNodeValue();
				firmName = entry.getElementsByTagName("firm_name").item(0).getFirstChild().getNodeValue();
				if (!result.toString().equals("ok")) {
					Util.log(logApplicationTag, logClassTag, "HTTP Result Fail: " + result.toString());
					cursor.moveToNext();	
					continue;
				}

				Element keyWordEntry = (Element) entry.getElementsByTagName("keyword1").item(0);
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
				
				int spamLevel = Integer.parseInt(spamGrade);
				// DB Update
		    	ContentValues contentValues = new ContentValues();			    	
		    	contentValues.put(CallLogs.NAME, firmName.toString());
	    		contentValues.put(CallLogs.SPAM_LEVEL, spamLevel);
	    		contentValues.put(CallLogs.SPAM_KEYWORD, keywordList.toString());	        		
	    		db.update(CallLogs.TABLE_NAME, contentValues, 
	    				  "number = ? and date = ? and sms_content = ?",
	    				  new String[] { inboxAddress, inboxTime.toString(), inboxContent });
	    		Util.log(logApplicationTag, logClassTag, "DB Udate, number=" + inboxAddress + 
	    				 ", date=" + inboxTime + ", sms_content=" + inboxContent);
	    		    		
	    		Util.log(logApplicationTag, logClassTag, "spamLevel=" + spamLevel + ", smsFilterLevel=" + smsFilterLevel);
	    		if (spamLevel >= smsFilterLevel) {
	    			this.abortBroadcast();
	    			Util.log(logApplicationTag, logClassTag, "Delete Inbox SMS" +
	    				     ", inboxAddress=" + inboxAddress + 
	    				     ", inboxContent=" + inboxContent);	    				     
	    		}	    		
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			cursor.moveToNext();	
		}
		
		cursor.close();
		db.close();
	
		return 0;
	}
}
