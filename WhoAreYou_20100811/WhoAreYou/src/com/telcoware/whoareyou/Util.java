package com.telcoware.whoareyou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.telcoware.whoareyou.PrivateContactDb.PrivateContacts;

public class Util {	
	
	static int isAvailableNetwork(Context context, int networkType) {
		//Check Network Status
		Toast toast;
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		String toastMessage = context.getResources().getString(R.string.failed_spam_search) + "\n" + 
		context.getResources().getString(R.string.check_network);
		boolean wifiEnable = (networkInfo.isAvailable() && networkInfo.isConnected());
		networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean mobileEnable = (networkInfo.isAvailable() && networkInfo.isConnected());
		if (networkType == 0) {
			if (!wifiEnable && !mobileEnable) {
				toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);            					
				toast.setGravity(Gravity.BOTTOM, 0, 90);
				toast.show();
				return -1;
			}
			return 0;
		}
		if (!wifiEnable) {
			toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);            					
			toast.setGravity(Gravity.BOTTOM, 0, 90);
			toast.show();
			return -1;
		}                   		            			
		return 0;
	}
	
	static int ContactExist(Context context, String phoneNumber) {
    	String[] projection = new String[] {
    			ContactsContract.PhoneLookup.DISPLAY_NAME,
    			ContactsContract.PhoneLookup.NUMBER };
		
    	Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)); 
		Cursor c = context.getContentResolver().query(contactUri, projection, null, null, null);
 		
		PrivateContactDbHelper dbHelper = new PrivateContactDbHelper(context);					
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = PrivateContacts.NUMBER + "=" + phoneNumber;
		Cursor c2 = db.query(PrivateContacts.TABLE_NAME, null, where, null, null, null, null);
		
		if (c.getCount()==0 && c2.getCount()==0)
		{
			c.close();
			c2.close();
			db.close();
			return -1;	
		}
		else
		{
			c.close();
			c2.close();
			db.close();
			return 0;
		}
		
    }
	
	static Document queryHTTP(String feed, int timeOut) {
    	URL url;
    	
    	try {
    		url = new URL(feed);
    		URLConnection connection = url.openConnection();
    		HttpURLConnection httpConnection = (HttpURLConnection)connection;
    		httpConnection.setConnectTimeout(timeOut);
    		int responseCode = httpConnection.getResponseCode();
    		if (responseCode == HttpURLConnection.HTTP_OK) {
    			InputStream in = httpConnection.getInputStream();

    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(in);
				return dom;
    		}
    		else {
				return null;
    		}		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return null;
	}
	
	// 안택수 추가
	// String 형태로 html 문서 리턴
	static String getWebDocument(String strAddr, int timeOut)
	{
		StringBuilder html = new StringBuilder();
		try
		{
			URL url = new URL(strAddr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(timeOut);
			if(conn != null)
			{
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					
					for(;;)
					{
						String line = br.readLine();
						if(line == null)
							break;
						html.append(line + '\n');
					}
					br.close();
				}
				conn.disconnect();
			}
		}
		catch (Exception ex) {;}
		return html.toString();
	}
	// 안택수 추가 부분 끝
	
	static Spanned getSpamLevelHtml(int spamLevel) {
		if (spamLevel == 0) {
			return Html.fromHtml("(Lv.<font color=#FFAAAA><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 1) {
			return Html.fromHtml("(Lv.<font color=#FF9999><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 2) {
			return Html.fromHtml("(Lv.<font color=#FF8888><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 3) {
			return Html.fromHtml("(Lv.<font color=#FF7777><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 4) {
			return Html.fromHtml("(Lv.<font color=#FF6666><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 5) {
			return Html.fromHtml("(Lv.<font color=#FF5555><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 6) {
			return Html.fromHtml("(Lv.<font color=#FF4444><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 7) {
			return Html.fromHtml("(Lv.<font color=#FF3333><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 8) {
			return Html.fromHtml("(Lv.<font color=#FF2222><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 9) {
			return Html.fromHtml("(Lv.<font color=#FF1111><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel >= 10) {
			return Html.fromHtml("(Lv.<font color=#FF0000><b>" + spamLevel + "</b></font>)");
		} else {
			return Html.fromHtml("Lv. Unknown");
		}
	}
	
	static Spanned getSpamLevelHtmlWithIndex(Context context, int spamLevel) {
		if (spamLevel == 0) {
			return Html.fromHtml("<font color=#FFAAAA><b>" + context.getResources().getString(R.string.level_0) + "</b></font>(Lv.<font color=#FFAAAA><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 1) {
			return Html.fromHtml("<font color=#FF9999><b>" + context.getResources().getString(R.string.level_1) + "</b></font>(Lv.<font color=#FF9999><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 2) {
			return Html.fromHtml("<font color=#FF8888><b>" + context.getResources().getString(R.string.level_2) + "</b></font>(Lv.<font color=#FF8888><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 3) {
			return Html.fromHtml("<font color=#FF7777><b>" + context.getResources().getString(R.string.level_3) + "</b></font>(Lv.<font color=#FF7777><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 4) {
			return Html.fromHtml("<font color=#FF6666><b>" + context.getResources().getString(R.string.level_4) + "</b></font>(Lv.<font color=#FF6666><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 5) {
			return Html.fromHtml("<font color=#FF5555><b>" + context.getResources().getString(R.string.level_5) + "</b></font>(Lv.<font color=#FF5555><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 6) {
			return Html.fromHtml("<font color=#FF4444><b>" + context.getResources().getString(R.string.level_6) + "</b></font>(Lv.<font color=#FF4444><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 7) {
			return Html.fromHtml("<font color=#FF3333><b>" + context.getResources().getString(R.string.level_7) + "</b></font>(Lv.<font color=#FF3333><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 8) {
			return Html.fromHtml("<font color=#FF2222><b>" + context.getResources().getString(R.string.level_8) + "</b></font>(Lv.<font color=#FF2222><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel == 9) {
			return Html.fromHtml("<font color=#FF1111><b>" + context.getResources().getString(R.string.level_9) + "</b></font>(Lv.<font color=#FF1111><b>" + spamLevel + "</b></font>)");
		} else if (spamLevel >= 10) {
			return Html.fromHtml("<font color=#FF0000><b>" + context.getResources().getString(R.string.level_10) + "</b></font>(Lv.<font color=#FF0000><b>" + spamLevel + "</b></font>)");
		} else {
			return Html.fromHtml(" ");
		}
	}
	
	static Drawable getSpamLevelImage(Context context, int spamLevel) {
		if (spamLevel == 0) {
			return context.getResources().getDrawable(R.drawable.level_0);
		} else if (spamLevel == 1) {
			return context.getResources().getDrawable(R.drawable.level_1);
		} else if (spamLevel == 2) {
			return context.getResources().getDrawable(R.drawable.level_2);
		} else if (spamLevel == 3) {
			return context.getResources().getDrawable(R.drawable.level_3);
		} else if (spamLevel == 4) {
			return context.getResources().getDrawable(R.drawable.level_4);
		} else if (spamLevel == 5) {
			return context.getResources().getDrawable(R.drawable.level_5);
		} else if (spamLevel == 6) {
			return context.getResources().getDrawable(R.drawable.level_6);
		} else if (spamLevel == 7) {
			return context.getResources().getDrawable(R.drawable.level_7);
		} else if (spamLevel == 8) {
			return context.getResources().getDrawable(R.drawable.level_8);
		} else if (spamLevel == 9) {
			return context.getResources().getDrawable(R.drawable.level_9);
		} else if (spamLevel >= 10) {
			return context.getResources().getDrawable(R.drawable.level_10);
		} else {
			return context.getResources().getDrawable(R.drawable.level_0);
		}
	}
	
	//spamKeywords format: keyword1:num1,keyword2:num2,keyword3:num3
	static Spanned getTopSpamKeywordHtml(String spamKeywords)
	{
		if (spamKeywords.length() <= 0) {
			return Html.fromHtml(" ");
		}
		
		String[] keywords = spamKeywords.split(",");
		if (keywords.length <= 0) {
			return Html.fromHtml(" ");
		}
		
		String[] keys = new String[keywords.length];
		String[] values = new String[keywords.length] ;
		
		for (int i = 0; i < keywords.length; i++) {
			String[] splitKeyValue = keywords[i].split(":");
			if (splitKeyValue.length != 2) {
				keys[i] = new String("Unknown");
				values[i] = new String("-1");
			}
			else {
				keys[i] = new String(splitKeyValue[0]);
				values[i] = new String(splitKeyValue[1]);
			}
		}
		
		int maxValue = 0;
		int curValue = 0;
		int maxIdx = 0;
		for (int i = 0; i < keywords.length; i++) {
			curValue = Integer.parseInt(values[i]);
			if (maxValue < curValue) {
				maxValue = curValue;
				maxIdx = i;
			}
		}
		
		if (Integer.parseInt(values[maxIdx]) == -1) {
			return Html.fromHtml(" ");
		}
		else {
			return Html.fromHtml(keys[maxIdx] + "(<font color=#ff8a19><b>" + values[maxIdx] + "</b></font>)");
		}
	}
	
	static Spanned getSpamKeywordHtml(String keywords)
	{
		String spamKeywords = "";

		if (keywords.length() <= 0 || keywords.equals(" ") ||
				keywords.equals("Unknown")) {
			return Html.fromHtml("키워드: 없음");
		}
		
		String[] keywordArray = keywords.split(",");
		if (keywordArray.length <= 0) {
			return Html.fromHtml("키워드 : 없음");
		}

		spamKeywords += "키워드 ";
		for (int i = 0; i < keywordArray.length; i++) {
			spamKeywords += "<br>";
			String[] splitKeyValue = keywordArray[i].split(":");
			if (splitKeyValue.length != 2) {
				continue;
			}
			else {
//				spamKeywords += "\t" + (i + 1) + " 순위: " + 
//								splitKeyValue[0] + "(" + "<font color=#ff8a19>" + splitKeyValue[1] + "</font>" + ")";
				spamKeywords += (i + 1) + ". " + 
				splitKeyValue[0] + "(" + "<font color=#ff8a19>" + splitKeyValue[1] + "</font>" + ")";
			}
		}
		
		return Html.fromHtml(spamKeywords);
	}
	
	static String convertKey(String number) {
		if (number != null) {
			String key = new StringBuffer(number).reverse().toString();
			
			while (key.length() < 8) {
				key = key + number + key;
			}
			
			byte buf[] = key.getBytes();
			byte keyChar[] = {0x01, 0x03, 0x15, 0x08, 0x01, 0x05, 0x02, 0x09};
			for (int i=0; i < key.getBytes().length; i++) {
				buf[i] = (byte) (buf[i] ^ keyChar[i % 8]);
			}
			key = new String(buf);
			key = key.replace("&", "A");
			key = key.replace("?", "DF");
			key = key.replace("%", "P");
			key = key.replace(" ", "_");
			key = key.replace("#", "S");
						
			return key;	
		}
		return "abcd";
	}
	
	static boolean isNumber(char c) {
		if (c == '0' || c == '1' ||	c == '2' ||
			c == '3' ||	c == '4' || c == '5' ||
			c == '6' || c == '7' ||	c == '8' || 
			c == '9') {
			return true;
		}
		else {
			return false;
		}
	}
	
	static void log(String logApplicationTag, String logClassTag, String logMessage) {
		Log.v(logApplicationTag, logClassTag + "::" + logMessage);
		return ;
	}
	
	static String getPhoneNumber(String number) {
		for (int i = 0; i < number.length(); i++) {
			if (Util.isNumber(number.charAt(i)) == true) {
				for (int j = i; j < number.length(); j++) {
					if(Util.isNumber(number.charAt(j)) == false) {
						return number.substring(i, j);
					}
				}
				return number;
			}
		}
		return number;
	}
}
