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

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
	public static ArrayList<String> TagFilter(InputStream in, ArrayList<String> tagList) {

		ArrayList<String> result = new ArrayList<String>();
		DocumentBuilderFactory dbf;
		NodeList items;
		Node item;
		Node text;
		try {

			dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(in);
			Element order = dom.getDocumentElement();
			
			for(int i = 0;i<tagList.size();i++){
					
				items = order.getElementsByTagName(tagList.get(i));
				item = items.item(0);
				text = item.getFirstChild();
				if( null == text){ 
					result.add(" ");}
				else{
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

//
//
//	public static ArrayList<ArrayList> ListTagFilter(InputStream in,
//			ArrayList<String> tagList) {
//
//		String temp;
//		try {
//
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//
//			Document dom = db.parse(in);
//			Element order = dom.getDocumentElement();
//
//			ArrayList<ArrayList> dataSet2 = new ArrayList<ArrayList>();
//			NodeList items = order.getElementsByTagName(tagList.get(0));
//
//			for (int i = 0; i < items.getLength() - 1; i++) {
//				ArrayList<String> datas = new ArrayList<String>();
//				for (int j = 1; j < tagList.size(); j++) {
//					NodeList items2 = order
//							.getElementsByTagName(tagList.get(j));
//					// ArrayList<String> datas= new ArrayList<String>();
//					// for(int k=0;k<items2.getLength()-1;k++){
//					temp = items2.item(i).getFirstChild().getNodeValue();
//					datas.add(temp);
//					// }
//				}
//				datas.clear();
//				dataSet2.add(datas);
//			}
//			return dataSet2;
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//		}
//
//		return null;
//	}

	//
	// public static ArrayList<String> TagFilterList(ArrayList<String> in,
	// String tag){
	//		
	// DocumentBuilderFactory dbf;
	// String result;
	// InputStream istream;
	// Document dom;
	// Element order;
	// NodeList items;
	// String temp;
	// Node text;
	// ArrayList<String> result;
	//		
	// String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	// //in = xml + in;
	//	
	// try {
	//			
	// dbf = DocumentBuilderFactory.newInstance();
	// DocumentBuilder db = dbf.newDocumentBuilder();
	//		
	// temp = xml + in.get(0);
	// istream = new ByteArrayInputStream(temp.getBytes("utf-8"));
	//			
	// dom = db.parse(istream);
	// order = dom.getDocumentElement();
	// items = order.getElementsByTagName(tag);
	//			
	// text = items.item(0).getFirstChild();
	// result.add(text.getNodeValue());
	//	
	// return result;
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// }
	//
	// return null;
	// }
}
