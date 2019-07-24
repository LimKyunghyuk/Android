package kr.priv.test3;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class Dialog {

	private static String TAG = "err";

	public static ArrayList<String> makeDialog(final Context context, ArrayList<String> oriWord,
			ArrayList<String> changedWord) {

		for (int i = 0; i < oriWord.size(); i++) {

			if (oriWord.get(i).contains("#br#")) {
			}
		}

		// ArrayList로 받은 아이템들을 스트링 배열로 변환
		String[] listMenu2 = oriWord.toArray(new String[oriWord.size()]);

		// 기본값은 모두 체크 해제
		boolean[] boolArr = new boolean[oriWord.size()];

		for (int i = 0; i < oriWord.size(); i++) {
			boolArr[i] = false;
		}

		//int[] resultArray = new int[oriWord.size()];

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("리스트");

		builder.setMultiChoiceItems(listMenu2, boolArr,
				new DialogInterface.OnMultiChoiceClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton, boolean isChecked) {

						Log.e("TAG", "whichButton:" + whichButton);
						Log.e("TAG", "isChecked:" + isChecked);
						
						makeRadioDialog(context);
					}

				});

		builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Log.e("TAG", "start");

			}

		});

		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.e("TAG", "cencel");
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

		return oriWord;
	}

	private static void makeRadioDialog(Context context) {

		String[] listMenu2 = { "사과2", "딸2기", "바나2나" };
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("리스트");

		builder.setSingleChoiceItems(listMenu2, -1,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {

					}

				});

		
		builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Log.e("TAG", "start");

			}

		});

		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.e("TAG", "cencel");
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();

	}
}
