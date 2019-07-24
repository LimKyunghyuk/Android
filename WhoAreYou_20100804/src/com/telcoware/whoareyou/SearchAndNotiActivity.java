package com.telcoware.whoareyou;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.telcoware.whoareyou.CallLogDb.CallLogs;


public class SearchAndNotiActivity extends Activity {
	
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;

	SharedPreferences devConfigPrefs;
	
	String spamGrade;
	String keyword1, keyword2, keyword3;
	String keyword1Grade, keyword2Grade, keyword3Grade;
	String firmName;
	TextView spamGradeTextView, keywordTextView, firmTextView;
	TextView spamGradeView, keywordView, firmView;
	ImageView spamGradeImageView;
	long rowId;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spam_result);
        context = getApplicationContext();

		telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			public void onCallStateChanged(int callState, String inComingNumber) {               
            	switch (callState) {
        		case TelephonyManager.CALL_STATE_IDLE:
        			finish();
            	}  
    		}
		};
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    	
        // Get DevPreferences
		try {
			devConfigPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        /*
        Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.spam_result); */
 
    	spamGradeTextView = (TextView) 		findViewById(R.id.spamGradeTextView);
		keywordTextView = (TextView) findViewById(R.id.keywordTextView);
		firmTextView = (TextView) findViewById(R.id.firmTextView);
	   	spamGradeView = (TextView) findViewById(R.id.spamGradeView);
		keywordView = (TextView) findViewById(R.id.keywordView);
		firmView = (TextView) findViewById(R.id.firmView);
		spamGradeImageView = (ImageView) findViewById(R.id.spamGradeImageView);
	
		Bundle extras = this.getIntent().getExtras();
        String callId = extras.getString("cid");
        rowId = extras.getLong("rowId");

		/* reset form */
		spamGradeTextView.setText("");
		keywordTextView.setText("");
		firmTextView.setText("");
		spamGradeView.setText("");
		keywordView.setText("");
		firmView.setText("");
		
        spamGradeTextView.setText(R.string.searching_spam_db);
        if (searchSpamCall(callId) < 0) {
        	spamGradeTextView.setText(R.string.failed_spam_search);
			firmTextView.setText(R.string.check_network);
        }

        /*
        d.setCanceledOnTouchOutside(true);
        d.show();*/
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
  
	private int searchSpamCall(String callId)
    {		
    	String result, keywordList = " ", keywordDisplay = " ";
    	int spamLevel;
    	
    	try {
    		String spamFeed;
    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
    		{	
    			spamFeed = getString(R.string.spam_feed);
    		} else
    		{
    			spamFeed = getString(R.string.spam_feed_outter);
    		}
    		
    		spamFeed = spamFeed + callId + "&key=" + Util.convertKey(callId);
    		
    		Document dom = Util.queryHTTP(spamFeed, 4000);
    		if (dom != null) {
				Element docEle = dom.getDocumentElement();

				Element entry = (Element)docEle.getElementsByTagName("spam_call").item(0);
				
				result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
				spamGrade = entry.getElementsByTagName("spam_grade").item(0).getFirstChild().getNodeValue();
				firmName = entry.getElementsByTagName("firm_name").item(0).getFirstChild().getNodeValue();

				/* reset form */
				spamGradeTextView.setText("");
				keywordTextView.setText("");
				firmTextView.setText("");
				spamGradeView.setText("");
				keywordView.setText("");
				firmView.setText("");
				
				spamGradeView.setText(R.string.spam_level);
				keywordView.setText(R.string.spam_keyword);
				firmView.setText(R.string.firm_name);
				if (result.toString().equals("ok")) {
					Element keyWordEntry = (Element) entry.getElementsByTagName("keyword1").item(0);
					keyword1 = keyWordEntry.getFirstChild().getNodeValue();					keyword1Grade = keyWordEntry.getAttribute("grade");
	
					keyWordEntry = (Element) entry.getElementsByTagName("keyword2").item(0);
					keyword2 = keyWordEntry.getFirstChild().getNodeValue();					keyword2Grade = keyWordEntry.getAttribute("grade");
					
					keyWordEntry = (Element) entry.getElementsByTagName("keyword3").item(0);
					keyword3 = keyWordEntry.getFirstChild().getNodeValue();					keyword3Grade = keyWordEntry.getAttribute("grade");
					
					spamLevel = Integer.parseInt(spamGrade);
					spamGradeImageView.setImageDrawable(Util.getSpamLevelImage(context, spamLevel));
					spamGradeTextView.setText(Util.getSpamLevelHtml(spamLevel));
					
					if (!keyword1.toString().equals(" ")) {			
						keywordDisplay = keyword1 + "(<font color=#ff8a19><b>" + keyword1Grade + "</b></font>)";
						keywordList = keyword1 + ":" + keyword1Grade;
						if (!keyword2.toString().equals(" ")) {
							keywordDisplay = keywordDisplay + "<br>" + keyword2 + "(<font color=#ff8a19><b>" + keyword2Grade + "</b></font>)";
							keywordList = keywordList + ","  +  keyword2 + ":" + keyword2Grade;
						}
						if (!keyword3.toString().equals(" ")) {
							keywordDisplay = keywordDisplay + "<br>" + keyword3 + "(<font color=#ff8a19><b>" + keyword3Grade + "</b></font>)";
							keywordList = keywordList + "," + keyword3 + ":" + keyword3Grade;
						}
						
						keywordTextView.setText(Html.fromHtml(keywordDisplay));
					}
					else {
						keywordTextView.setText(Html.fromHtml("없음"));
					}
					
					if (!firmName.toString().equals(" ")) {
						firmTextView.setText(Html.fromHtml(firmName));
					}
					else {
						firmTextView.setText(Html.fromHtml("없음"));
					}
				}
				else {					
					spamGradeTextView.setText(Html.fromHtml("없음"));
					keywordTextView.setText(Html.fromHtml("없음"));
					firmTextView.setText(Html.fromHtml("없음"));
					spamGradeImageView.setImageDrawable(null);
					spamLevel = -1;
				}
								
				/* update DB */
				if (rowId > 0) {
			    	ContentValues contentValues = new ContentValues();
			    	CallLogDbHelper callLogDbHelper = new CallLogDbHelper(this.getApplicationContext());
			    	SQLiteDatabase callLogDb = callLogDbHelper.getWritableDatabase();;
			    	
			    	contentValues.put(CallLogs.NAME, firmName.toString());
	        		contentValues.put(CallLogs.SPAM_LEVEL, spamLevel);
	        		contentValues.put(CallLogs.SPAM_KEYWORD, keywordList.toString());
	        		
	        		callLogDb.update(CallLogs.TABLE_NAME, contentValues, "_id = ?", new String[] { new Long(rowId).toString() });
	        			        		
        			if(callLogDb != null)
        			{
        				callLogDb.close();
        			}
	        			
				}
				return 0;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return -1;
    }
}