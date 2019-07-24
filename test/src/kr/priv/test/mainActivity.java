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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
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
	String url1 = "http://mane.nasse.net/test1.php";
	String url2 = "http://mane.nasse.net/test2.php";
	
	ArrayList<String> prmtr = new ArrayList<String>();

	
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
		Toast.makeText(getApplicationContext(), "시작은하나?",
				Toast.LENGTH_SHORT).show();

		View a = findViewById(R.id.btn_1);
		a.setOnClickListener(this);

		edt1 = (EditText) findViewById(R.id.edt_1);
		edt2 = (EditText) findViewById(R.id.edt_2);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		str1 = edt1.getText().toString();
		str2 = edt2.getText().toString();

		switch (v.getId()) {
		case R.id.btn_1:

			Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT)
			.show();

			prmtr.add("http://mycom.priv.kr/a.php");
			//prmtr.add("4");
			//prmtr.add("0");
			//prmtr.get(0);
			
			ArrayList<String> tagList = new ArrayList<String>();
			
			tagList.add("driver");
			tagList.add("aaa");
			tagList.add("bbb");
			tagList.add("ccc");
			
			ArrayList<ArrayList> result = new ArrayList<ArrayList>();
			
			
			result.add(HttpConnect.ListTagFilter(HttpConnect.postData(prmtr),tagList));
			
			 PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
		        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		        wl.acquire();  
			
			
			//String testStr = HttpConnect.TagFilter((HttpConnect.postData(prmtr)), "user_join");
			
			Toast.makeText(getApplicationContext(), (String)result.get(0).get(0), Toast.LENGTH_SHORT)
			.show();

			
		}

	}
}