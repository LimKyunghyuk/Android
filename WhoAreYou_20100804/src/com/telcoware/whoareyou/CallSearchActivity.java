package com.telcoware.whoareyou;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CallSearchActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "CallSearchActivity";
	
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	SharedPreferences devConfigPrefs;
	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;
	ConnectivityManager connectivityManager;
	NetworkInfo networkInfo;
	
	String spamGrade;
	String keyword1, keyword2, keyword3;
	String keyword1Grade, keyword2Grade, keyword3Grade;
	String firmName;
	TextView spamGradeTextView, keywordTextView, firmTextView;
	TextView spamGradeView, keywordView, firmView;	
	ImageView spamGradeImageView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.call_search);
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
    	spamGradeTextView = (TextView) findViewById(R.id.search_spamGradeTextView);
		keywordTextView = (TextView) findViewById(R.id.search_keywordTextView);
		firmTextView = (TextView) findViewById(R.id.search_firmTextView);
	   	spamGradeView = (TextView) findViewById(R.id.search_spamGradeView);
		keywordView = (TextView) findViewById(R.id.search_keywordView);
		firmView = (TextView) findViewById(R.id.search_firmView);
		spamGradeImageView = (ImageView) findViewById(R.id.search_spamGradeImageView);
		
	    final EditText phoneNumber;
        phoneNumber = (EditText)findViewById(R.id.search_number);       
		final RadioGroup rg = (RadioGroup)findViewById(R.id.radio_group);
		
        Button searchButton = (Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				
				/* reset form */
				spamGradeTextView.setText("");
				keywordTextView.setText("");
				firmTextView.setText("");
				spamGradeView.setText("");
				keywordView.setText("");
				firmView.setText("");
				spamGradeImageView.setImageDrawable(null);
				
				
				//Check Network Status
        		if (Util.isAvailableNetwork(context, configPrefs.getInt("network", 0)) < 0) {
        			return;
        		}
				
				String keyword = phoneNumber.getText().toString();
				String type = "phone";
				if (rg.getCheckedRadioButtonId() == R.id.radio_sms) {
					type = "sms";
				}
				if (keyword.length() <= 0) {
					return ;
				}
				
				// Check membership
				if (configPrefs.getBoolean("certification", false) == false) {
					Util.log(logApplicationTag, logClassTag, "this user hasn't a membership. servece is unavailable.");
					
					new AlertDialog.Builder(CallSearchActivity.this)
					.setMessage(R.string.member_notok)
					.setPositiveButton("OK", null)
					.show();
					
					return;
				}
				else
				{
					spamGradeTextView.setText(R.string.searching_spam_db);
					if (searchSpamCall(keyword, type) < 0){
						spamGradeTextView.setText(R.string.failed_spam_search);
						firmTextView.setText(R.string.check_network);
					}
				}				
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// Get Preferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// Get DevPreferences	
		try {
			devConfigPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	//Internal Implementation	
    class SmsInboxContentObserver extends ContentObserver {
    	public SmsInboxContentObserver(Handler handler) {
            super(handler);
    	}

    	@Override
    	public boolean deliverSelfNotifications() {
            return false;
    	}

    	@Override
    	public void onChange(boolean arg0) {
            super.onChange(arg0);
            finish();
    	}
    }

	private int searchSpamCall(String callId, String type)
    {
		
    	URL url;
    	String result;
    	int spamLevel;
    	
    	// 가상키보드 강제로 숨기기
    	EditText myEditText = (EditText)findViewById(R.id.search_number);
		InputMethodManager ink = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		ink.hideSoftInputFromWindow(myEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
    	try {
    		String feed;
    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
    		{	
    			feed = getString(R.string.spam_feed);
    		} else
    		{
    			feed = getString(R.string.spam_feed_outter);
    		}
    		
    		String spamFeed = feed + callId + 
    							"&mode=search" + 
    							"&key=" + Util.convertKey(callId) +
    							"&type=" + type;
    		
    		url = new URL(spamFeed);
    		Util.log(logApplicationTag, logClassTag, "SpamFeed :" + spamFeed);
    		
    		URLConnection connection = url.openConnection();    		
    		HttpURLConnection httpConnection = (HttpURLConnection)connection;
    		
    		httpConnection.setConnectTimeout(5000);
    		int responseCode = httpConnection.getResponseCode();
    		if (responseCode == HttpURLConnection.HTTP_OK) {
    			InputStream in = httpConnection.getInputStream();
    			
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(in);
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
				if (result.toString().equals("ok")) {
					Element keyWordEntry = (Element) entry.getElementsByTagName("keyword1").item(0);
					keyword1 = keyWordEntry.getFirstChild().getNodeValue();
					keyword1Grade = keyWordEntry.getAttribute("grade");
	
					keyWordEntry = (Element) entry.getElementsByTagName("keyword2").item(0);
					keyword2 = keyWordEntry.getFirstChild().getNodeValue();
					keyword2Grade = keyWordEntry.getAttribute("grade");
					
					keyWordEntry = (Element) entry.getElementsByTagName("keyword3").item(0);
					keyword3 = keyWordEntry.getFirstChild().getNodeValue();
					keyword3Grade = keyWordEntry.getAttribute("grade");
										
					spamLevel = Integer.parseInt(spamGrade);
					spamGradeImageView.setImageDrawable(Util.getSpamLevelImage(context, spamLevel));
					spamGradeTextView.setText(Util.getSpamLevelHtml(spamLevel));

					if (!keyword1.toString().equals(" ")) {
						String keywordText;
						
						keywordView.setText(R.string.spam_keyword);
						keywordText = " &nbsp; 1순위 - " + keyword1 + "(<font color=#ff8a19>" + keyword1Grade + "</font>)";
						
						if (!keyword2.toString().equals(" ")) {
							keywordText = keywordText + "<br>&nbsp; 2순위 - " + keyword2 + "(<font color=#ff8a19>" + keyword2Grade + "</font>)";
						}
						if (!keyword3.toString().equals(" ")) {
							keywordText = keywordText + "<br>&nbsp; 3순위 - " + keyword3 + "(<font color=#ff8a19>" + keyword3Grade + "</font>)";
						}
						
						keywordTextView.setText(Html.fromHtml(keywordText));
					}			
				}
				else {					
					spamGradeImageView.setImageDrawable(null);
					spamGradeTextView.setText(Html.fromHtml("검색결과없음"));
				}
				if (!firmName.toString().equals(" ")) {
					firmView.setText(R.string.firm_name);
					firmTextView.setText(Html.fromHtml(firmName));
				}
				return 0;
    		}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return -1;
    }
}
