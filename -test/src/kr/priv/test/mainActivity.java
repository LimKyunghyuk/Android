package kr.priv.test;

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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class mainActivity extends Activity implements OnClickListener {

	Button btn;

	EditText edt1;
	EditText edt2;

	String str1 = "";
	String str2 = "";

	ArrayList<String> dbList;
	ArrayAdapter<String> Adapter;

	String val1 = "";

	String ret1 = "";
	String ret2 = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbList = new ArrayList<String>();
		dbList.add("abc");
		dbList.add("kkk");

		btn = (Button) findViewById(R.id.btn_1);

		Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dbList);

		ListView list = (ListView) findViewById(R.id.listlay);
		list.setAdapter(Adapter);
		Toast.makeText(getApplicationContext(), "시작은하나?2222",
				Toast.LENGTH_SHORT).show();

		View a = findViewById(R.id.btn_1);
		a.setOnClickListener(this);

		edt1 = (EditText) findViewById(R.id.edt_1);
		edt2 = (EditText) findViewById(R.id.edt_2);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT)
				.show();

		str1 = edt1.getText().toString();
		str2 = edt2.getText().toString();

		switch (v.getId()) {
		case R.id.btn_1:

			// 데이터를 웹서버에 보내고 받아온 결과를 출력합니다.
			Toast.makeText(getApplicationContext(), "버튼 누르기",
					Toast.LENGTH_SHORT).show();

			postData(str1, str2);

		}

		// new FileUpLoad().execute();

	}

	// 데이터를 전송하고 결과를 리턴한다.
	
	public ArrayList<String> getData(){
		
		ArrayList<String> result = null;
		
		
		
		
		return result;
	}
	
	
	public InputStream postData(String i, String c) {

		InputStream result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://mycom.priv.kr/test.php");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("val1", i));
			nameValuePairs.add(new BasicNameValuePair("val2", c));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			Toast.makeText(getApplicationContext(), "전송완료", Toast.LENGTH_SHORT)
					.show();
			
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

	private class FileUpLoad extends AsyncTask<String, Integer, Long> {

		// 작업 시작을 UI에 알림
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast
					.makeText(getApplicationContext(), "전송 시작",
							Toast.LENGTH_SHORT).show();

		}

		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
		@Override
		protected Long doInBackground(String... strData) {
			// TODO Auto-generated method stub
			long totalTimeSpent = 0;

			// 가변인자의 갯수
			int numberOfParams = strData.length;

			return totalTimeSpent;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}
}