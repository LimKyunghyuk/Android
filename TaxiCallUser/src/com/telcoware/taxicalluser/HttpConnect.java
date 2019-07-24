package com.telcoware.taxicalluser;

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
	public static InputStream postData(String... s) {

		InputStream result = null;
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(s[0]);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			for (int i = 1; i < s.length; i++) {
				nameValuePairs.add(new BasicNameValuePair("val" + i, s[i]));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

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
	public static ArrayList<String> TagFilter(InputStream in, String... s) {

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

			for (int i = 0; i < s.length; i++) {

				items = order.getElementsByTagName(s[i]);
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

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

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

	public static ArrayList<CTaxiDriver> ListTagFilter(InputStream in,
			ArrayList<String> tagList) {
		int TAGCNT = tagList.size();

		// STring = TexiData;
		ArrayList<CTaxiDriver> dataSet = new ArrayList<CTaxiDriver>();
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

			items[0] = order.getElementsByTagName("operator");
			if ("nok" == items[0].item(0).getFirstChild().getNodeValue())
				return null;

			// 문서를 테그 이름으로 구분하여 해당 리스트 담는다.
			for (int i = 0; i < TAGCNT; i++) {
				items[i] = order.getElementsByTagName(tagList.get(i));
			}

			// 만약 발견되는 테그가 없다면
			if (0 == items[0].getLength())
				return null;

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

				CTaxiDriver driver = new CTaxiDriver(prmtr[3], prmtr[6]);
				driver.setLicense(prmtr[0]);
				driver.setTaxiNumber(prmtr[1]);
				driver.setPhone(prmtr[2]);
				driver.setType(Integer.parseInt(prmtr[4]));
				driver.setCompany(prmtr[5]);
				driver.setPaymentType(Integer.parseInt(prmtr[8])); // / 지불방법
				driver.setGrade(Float.valueOf(prmtr[9]).floatValue()); // / 등급
				driver.setLatitude((int) (Double.parseDouble(prmtr[10]) * 1E6));
				driver.setLongitude((int) (Double.parseDouble(prmtr[11]) * 1E6));
				driver.setImgURL(prmtr[14]); // / 이미지URL

				dataSet.add(driver);
			}

			return dataSet;
		} catch (Exception e) {

			return null;
		}
	}
	
	public static ArrayList<EstData> estListTagFilter(InputStream in,
			ArrayList<String> tagList) {
		int TAGCNT = tagList.size();

		// STring = TexiData;
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

			items[0] = order.getElementsByTagName("estimation");
			if ("nok" == items[0].item(0).getFirstChild().getNodeValue())
				return null;

			// 문서를 테그 이름으로 구분하여 해당 리스트 담는다.
			for (int i = 0; i < TAGCNT; i++) {
				items[i] = order.getElementsByTagName(tagList.get(i));

			}

			// 만약 발견되는 테그가 없다면

			if (0 == items[0].getLength())
				return null;

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