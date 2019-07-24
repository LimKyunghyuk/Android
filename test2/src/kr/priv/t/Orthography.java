package kr.priv.t;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.util.Log;

public class Orthography {

	private final static String TAG = "msg";
	static TagNode node;
	static HtmlCleaner cleaner;
	static CleanerProperties props;
	static Object[] titleArray;
	static ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	static ArrayList<String> str = new ArrayList<String>();

	public static ArrayList<String> check(String cleanPage) {

		cleaner = new HtmlCleaner();

		Log.d(TAG, "push");
		try {

			node = cleaner.clean(cleanPage);

			try {

				Log.d(TAG, "!!!!!!!!!!!!!!!!!!");

				// titleArray =
				// node.evaluateXPath("//td[@id='tdReplaceWord_0'@align='center'@style='color:#0000CD;font-weight:bold']");
				titleArray = node.evaluateXPath("//td");
				int i = 1;

				int oriNum = 4, modNum = 6;

				TagNode t2;
				for (Object obj : titleArray) {
					t2 = (TagNode) obj;

					if (2 == i && t2.getText().toString().contains("문법")) {
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

					Log.d(TAG, "[" + i + "][" + t2.getText().toString() + "]");
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

		return str;
	}

}
