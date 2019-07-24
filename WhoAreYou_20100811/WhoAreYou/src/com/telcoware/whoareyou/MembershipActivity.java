package com.telcoware.whoareyou;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class MembershipActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "MembershipActivity";
	
	String PhoneNumber = "";
	
	private WebView mWebView;
	SharedPreferences devConfigPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.membership);
		
		TelephonyManager telephony = (TelephonyManager)getSystemService(Activity.TELEPHONY_SERVICE);
		PhoneNumber = telephony.getLine1Number();
		
		// Get DevPreferences	
		try {
			devConfigPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Button btnJoin = (Button)findViewById(R.id.memberjoin);
		Button btnCancel = (Button)findViewById(R.id.memberjoincancel);
		TextView txtID = (TextView)findViewById(R.id.memberid);
		txtID.setText(PhoneNumber);
		
		//txtID.setText(text)
		
		// 가상키보드 강제로 숨기기
//		InputMethodManager ink = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
//		ink.hideSoftInputFromWindow(txtID.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		btnJoin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {
					TextView id = (TextView)findViewById(R.id.memberid);
					
					String result = "";
					
					String join_feed;
		    		if (devConfigPrefs.getInt("dev_network", 1) == 0)
		    		{	
		    			join_feed = getString(R.string.member_join_feed);
		    		} else
		    		{
		    			join_feed = getString(R.string.member_join_feed_outter);
		    		}
		    		
		    		String feed = join_feed + id.getText() + 
		    					"&manufacturer=" + URLEncoder.encode(android.os.Build.MANUFACTURER, "UTF-8") + 
		    					"&model=" + URLEncoder.encode(android.os.Build.MODEL, "UTF-8") + 
		    					"&sdk_ver=" + Integer.parseInt(Build.VERSION.SDK) +
		    					"&locale=" + getApplicationContext().getResources().getConfiguration().locale.getISO3Language();
		    		
		    		Util.log(logApplicationTag, logClassTag, "member join query url : " + feed);
					URL url = new URL(feed);
		    		
		    		URLConnection connection = url.openConnection();
		    		HttpURLConnection httpConnection = (HttpURLConnection)connection;
		    		
		    		httpConnection.setConnectTimeout(5000);
		    		int responseCode = httpConnection.getResponseCode();
		    		Util.log(logApplicationTag, logClassTag, "response code : " + responseCode);
		    		if (responseCode == HttpURLConnection.HTTP_OK) {
		    			InputStream in = httpConnection.getInputStream();
		    			
		    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						
						Document dom = db.parse(in);
						Element docEle = dom.getDocumentElement();
	
						Element entry = (Element)docEle.getElementsByTagName("user_join").item(0);
						
						result = entry.getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
						Util.log(logApplicationTag, logClassTag, "Member Join : " + id.getText() + ", Result : " + result);
												
						if (result.equalsIgnoreCase("ok") == true)
						{	
							Intent data = new Intent();
							data.putExtra("result", 1);
							setResult(RESULT_OK, data);
			                finish();   
						}
						else if (result.equalsIgnoreCase("nok") == true)
						{
							Intent data = new Intent();
							data.putExtra("result", 0);
							setResult(RESULT_OK, data);
			                finish();   
						}
						else //if (result.equalsIgnoreCase("na") == true)
						{ 
							Intent data = new Intent();
							data.putExtra("result", -1);
							setResult(RESULT_OK, data);
			                finish();   
						}
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
				
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Util.log(logApplicationTag, logClassTag, "Memberjoin Canceled");
				setResult(RESULT_CANCELED, null);
                finish();
			}
		});
		
		mWebView = (WebView) findViewById(R.id.webview);
		
		String url;
		if (devConfigPrefs.getInt("dev_network", 1) == 0)
		{	
			url = getString(R.string.term_site);
		} else
		{
			url = getString(R.string.term_site_outter);
		}
				
		Util.log(logApplicationTag, logClassTag, "term site :" + url);
		
		mWebView.loadUrl(url);
	}
}
