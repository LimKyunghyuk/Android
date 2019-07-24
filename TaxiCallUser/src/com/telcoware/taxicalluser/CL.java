package com.telcoware.taxicalluser;

import java.io.*;
import java.net.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class CL
{	
	// Html 문서 리턴
	public static String getWebDocument(String strAddr)
	{
		StringBuilder html = new StringBuilder();
		try
		{
			URL url = new URL(strAddr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
		catch (Exception ex){}
		return html.toString();
	}
	
	// Xml 파싱해서 엘리먼트 리턴
	public static Element parseXmlDocument(String _xmlDoc)
	{
		// Null값 체크할 것
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream istream = new ByteArrayInputStream(_xmlDoc.getBytes("utf-8"));
			Document doc = builder.parse(istream);
			return doc.getDocumentElement();
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	// URL 인코더
	public static String urlEncoder(String _url)
	{
		String convert = null;
		
		try
		{
			convert = URLEncoder.encode(_url, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return convert;
	}
	
	public static boolean isAvailableNetwork(Context context, int networkType)
	{
		//Check Network Status
		Toast toast;
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		String toastMessage = "네트워크 연결이 불가능합니다." + "\n" + "연결을 확인해 주세요";
		boolean wifiEnable = (networkInfo.isAvailable() && networkInfo.isConnected());
		networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean mobileEnable = (networkInfo.isAvailable() && networkInfo.isConnected());
		if (networkType == 0) {
			if (!wifiEnable && !mobileEnable) {
				toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);            					
				toast.setGravity(Gravity.BOTTOM, 0, 90);
				toast.show();
				return false;
			}
			return true;
		}
		if (!wifiEnable) {
			toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);            					
			toast.setGravity(Gravity.BOTTOM, 0, 90);
			toast.show();
			return false;
		}                   		            			
		return true;
	}
	
	public static String getAddress(int lat, int lon)
    {
    	String m_strXmlDoc = CL.getWebDocument("http://maps.google.co.kr/maps/api/geocode/xml?latlng=" + (double)(lat)/1E6 + "," + (double)(lon)/1E6 + "&sensor=true");
        NodeList listAddress = CL.parseXmlDocument(m_strXmlDoc).getElementsByTagName("formatted_address");
        
        String address = listAddress.item(0).getFirstChild().getNodeValue();
        address = address.replace("대한민국 ", "");

        return address;
    }
	
	// 터치 진동 반응
	static public void TouchVibrate(Context context)
	{
		Vibrator vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(30);
	}
}
