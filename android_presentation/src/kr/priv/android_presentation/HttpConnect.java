package kr.priv.android_presentation;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.protocol.*;
import org.w3c.dom.*;

//import android.widget.Toast;

public class HttpConnect {
	private static final Header[] HEADERS = {
			new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; "
					+ "Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)"),
			new BasicHeader("Accept-Language", "zh-cn"),
			new BasicHeader(
					"Accept",
					" image/gif, image/x-xbitmap, image/jpeg, "
							+ "image/pjpeg, application/x-silverlight, application/vnd.ms-excel, "
							+ "application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*"),
			new BasicHeader("Content-Type", "text/plain") };

	public static String postData(String URL) {

		InputStream result = null;
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(URL);
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("val"," "));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			httppost.setHeaders(HEADERS);

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			result = entity.getContent();

			 StringBuilder out = new StringBuilder();
             byte[] b = new byte[4096];
             for (int n; (n = result.read(b)) != -1;) {
                     out.append(new String(b, 0, n));
             }

             if (entity != null) {
                     entity.consumeContent();
             }

             return out.toString();
			

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	// Stream을 받아서 tag로 필터링 하여 String으로 변환
	public static ArrayList<String> TagFilter(InputStream in,
			ArrayList<String> tagList) {

		ArrayList<String> result = new ArrayList<String>();
		DocumentBuilderFactory dbf;
		NodeList items;
		Node item;
		Node text;
		try {
			// 
			dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(in);
			Element order = dom.getDocumentElement();

			for (int i = 0; i < tagList.size(); i++) {

				items = order.getElementsByTagName(tagList.get(i));
				item = items.item(0);
				text = item.getFirstChild();
				if (null == text) {
					result.add(" ");
				} else {
					result.add(text.getNodeValue());
				}
			}

			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	// Stream을 받아서 tag로 필터링 하여 String으로 변환

}