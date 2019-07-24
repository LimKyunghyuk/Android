package kr.priv.woorisms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

public class HttpConnect {

	public static InputStream postData(String url, String val) {

		InputStream result = null;
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(url);
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			// List에 전달할 인자를 하나씩 넣기
			nameValuePairs.add(new BasicNameValuePair("text1", val));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			// 전달
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

	// Stream을 받아서 String으로 변환
	public static String getString(InputStream in) {

		try {

			StringBuffer sb = new StringBuffer();
			byte[] b = new byte[4096000];
			for (int n; (n = in.read(b)) != -1;) {
				sb.append(new String(b, 0, n));
			}

			return sb.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return null;
	}
}
