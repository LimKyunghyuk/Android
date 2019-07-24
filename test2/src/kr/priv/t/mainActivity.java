package kr.priv.t;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class mainActivity extends Activity {

	Button btnChk;
	Button btnSnd;
	private final static String TAG = "msg";
	TagNode node;
	HtmlCleaner cleaner;
	CleanerProperties props;
	Object[] titleArray;
	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	ArrayList<String> str = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnChk = (Button) findViewById(R.id.btn_1);
		btnChk.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				cleaner = new HtmlCleaner();

				Log.d(TAG, "push");
				try {

					props = cleaner.getProperties();

					String url = "http://speller.cs.pusan.ac.kr/PnuSpellerISAPI_201009/lib/PnuSpellerISAPI_201009.dll?Check";
					String val = "apble 안녕하세용 apble";
					
					String result2 = HttpConnect.TagFilter((HttpConnect
							.postData(url, val)));

					Log.d("A",">>>>>"+result2);
					
					String result3 = ClearDocu.clean(result2);

					Log.d("A",">>>>>"+result3);
					
					node = cleaner.clean(result3);

					
					try {

						Log.d(TAG, "!!!!!!!!!!!!!!!!!!");
						titleArray = node.evaluateXPath("//td");
						int i = 1;

						int oriNum = 4, modNum = 6;
						TagNode t2;
						for (Object obj : titleArray) {
							t2 = (TagNode) obj;

							if (2 == i
									&& t2.getText().toString().contains("문법")) {
								Log.d(TAG, "오타가 발견되지 않았습니다.");
								break;
							}
							// 오타를 찾는 중

							if (oriNum == i) {
								str.add(t2.getText().toString());
								Log.d(TAG, "삽입");
								oriNum = oriNum + 10;
							}

							if (modNum == i) {
								str.add(t2.getText().toString());
								Log.d(TAG, "삽입");
								modNum = modNum + 10;
							}


							Log.d(TAG, "[" + i + "][" + t2.getText().toString()
									+ "]");
							i++;
						}

						Log.d(TAG, "!!!!!!!!!!!!!!!!!!");

					} catch (XPatherException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} catch (IOException e) {
					e.printStackTrace();

				}
				
				SimpleXmlSerializer se = new SimpleXmlSerializer(props);

				try {
					se.writeXmlToStream(node, ostream, "UTF-8");
				} catch (IOException e) {

				}
				Log.d(TAG, "결과");

				for (int j = 0; j < str.size(); j++) {
					Log.d(TAG, "[" + str.get(j) + "]");
				}

				// Log.d(TAG,"<end>" + ostream.toString() + "</end>");

			}

		});

	}
}