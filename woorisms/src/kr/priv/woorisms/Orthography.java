package kr.priv.woorisms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
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
		try {

			node = cleaner.clean(cleanPage);

			try {

				titleArray = node.evaluateXPath("//td");
				int i = 1, oriNum = 4, modNum = 6;

				TagNode t2;
				str.clear();
				
				
				for (Object obj : titleArray) {
					t2 = (TagNode) obj;

					if (2 == i && t2.getText().toString().contains("문법")) {
						Log.d(TAG, "오타가 발견되지 않았습니다.");
						break;
					}

					// 리스트에서 원문장만 골라서 리스트에 삽입
					if (oriNum == i) {
						str.add(t2.getText().toString());
						Log.d(TAG, "삽입");
						oriNum = oriNum + 10;
					}

					// 리스트에서 대치어만 골라서 리스트에 삽입
					if (modNum == i) {

						// 대치어가 없다면
						if (t2.getText().toString().contains("대치어 없음")) {
							str.add(t2.getText().toString()+"#BR#");
						} else {
							str.add(t2.getText().toString());

						}
						Log.d(TAG, "삽입");
						modNum = modNum + 10;
					}

					// Log.d(TAG, "[" + i + "][" + t2.getText().toString() +
					// "]");
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

		for (int j = 0; j < str.size(); j++) {
			Log.d(TAG, "[" + str.get(j) + "]");
		}

		return str;
	}

}
