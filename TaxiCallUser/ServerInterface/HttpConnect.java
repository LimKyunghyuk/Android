package kr.priv.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import android.widget.Toast;

public class HttpConnect {

	public static InputStream postData(ArrayList<String> prmtr) {

		InputStream result = null;
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(prmtr.get(0)); 
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			for (int i = 1; i < prmtr.size(); i++) {
				nameValuePairs.add(new BasicNameValuePair("val" + i, prmtr
						.get(i)));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			result = entity.getContent();

			return result;

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
	public static String TagFilter(InputStream in, String tag) {

		DocumentBuilderFactory dbf;
		String result;

		try {

			dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(in);
			Element order = dom.getDocumentElement();
			NodeList items = order.getElementsByTagName(tag);
			Node item = items.item(0);
			Node text = item.getFirstChild();
			result = text.getNodeValue();

			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

	public static ArrayList<TexiData> ListTagFilter(InputStream in, ArrayList<String> tagList) {
		int TAGCNT = 13;
		
		//STring = TexiData;
		ArrayList<TexiData> dataSet = new ArrayList<TexiData>();
		NodeList[] items = new NodeList[TAGCNT];
		Node item;
		Node text;
		
		String[] prmtr = new String[tagList.size()];
		for (int i = 0; i < TAGCNT; i++) {
			prmtr[i] = new String();
		}
		
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(in);
			Element order = dom.getDocumentElement();

			// 문서를 테그 이름으로 구분하여 해당 리스트 담는다.
			for (int i = 0; i < TAGCNT; i++) {
				items[i] = order.getElementsByTagName(tagList.get(i));
			}
			
			// 만약 발견되는 테그가 없다면 
			if(0 == items[0].getLength()) return null;
			
			
			// XML에서 발견된 license_number의 수만큼 반복
			for (int j = 0; j < items[0].getLength(); j++) {
				// 모든 리스트 순회
				for (int i = 0; i < TAGCNT; i++) {
					item = items[i].item(j);
					 text = item.getFirstChild();
					 
					if (null == text) {
						prmtr[i] = " ";
					} else {
						prmtr[i] = text.getNodeValue();
					}
					
				}

				dataSet.add(new TexiData(prmtr));
			}

			return dataSet;
		} catch (Exception e) {

			return null;
		}
	}
	
	public static ArrayList<EstData> estListTagFilter(InputStream in, ArrayList<String> tagList) {
		int TAGCNT = 6;
		
		//STring = TexiData;
		ArrayList<EstData> dataSet = new ArrayList<EstData>();
		NodeList[] items = new NodeList[TAGCNT];
		Node item;
		Node text;
		
		String[] prmtr = new String[tagList.size()];
		for (int i = 0; i < TAGCNT; i++) {
			prmtr[i] = new String();
		}
		
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(in);
			Element order = dom.getDocumentElement();

			// 문서를 테그 이름으로 구분하여 해당 리스트 담는다.
			for (int i = 0; i < TAGCNT; i++) {
				items[i] = order.getElementsByTagName(tagList.get(i));
			
			}
			
			// 만약 발견되는 테그가 없다면 
			if(0 == items[0].getLength()) return null;
			
			// XML에서 발견된 license_number의 수만큼 반복
			for (int j = 0; j < items[0].getLength(); j++) {
				// 모든 리스트 순회
				for (int i = 0; i < TAGCNT; i++) {
					item = items[i].item(j);
					 text = item.getFirstChild();
					 
					if (null == text) {
						prmtr[i] = " ";
					} else {
						prmtr[i] = text.getNodeValue();
					}
					
				}

				dataSet.add(new EstData(prmtr));
			}

			return dataSet;
		} catch (Exception e) {

			return null;
		}
	}
}
