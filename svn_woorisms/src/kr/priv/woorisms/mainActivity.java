package kr.priv.woorisms;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class mainActivity extends Activity {

	final String URL = "http://speller.cs.pusan.ac.kr/PnuSpellerISAPI_201009/lib/PnuSpellerISAPI_201009.dll?Check";
	static String text;

	Button btnChk;
	Button btnSnd;
	static EditText editText;
	static EditText editNum;
	private Menu mMenu;
	ArrayList<String> uncookedList;

	static final String[] temp = new String[] { "가나나난", "가나다라", "가가다라", "가낭다라",
			"가느뭐라", "가쉉나라" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);

		// recipient
		editNum = (EditText) findViewById(R.id.edt_num);
		editText = (EditText) findViewById(R.id.edt_main);

		// 맞춤법 검사 버튼
		btnChk = (Button) findViewById(R.id.btn_check);
		btnChk.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				text = editText.getText().toString();

				if (text.length() == 0) {
					Toast.makeText(getApplicationContext(), "내용을 입력하지 않았습니다",
							Toast.LENGTH_SHORT).show();

				} else {

					// ******* 왜 안 나오는가? 프로그래시브 바로 대체 . 즐겨찾기 참고
					// DialogProgress();
					//readyItem();
					String temp = cookItem();
				}
			}

		});

		// 전송 버튼
		btnSnd = (Button) findViewById(R.id.btn_send);
		btnSnd.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				text = editText.getText().toString();

				if (text.length() == 0) {
					Toast.makeText(getApplicationContext(), "내용을 입력하지 않았습니다",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(getApplicationContext(), "전송 중",
							Toast.LENGTH_SHORT).show();

					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage(editNum.getText().toString(), null,
							editText.getText().toString(), null, null);

					// 콜로그로 넘어감
					Intent intent = new Intent(Intent.ACTION_CALL_BUTTON, null);
					startActivity(intent);

					// sendSMS(text)
				}

			}

		});

	}

	private void DialogProgress() {
		ProgressDialog dialog = ProgressDialog.show(mainActivity.this, "검사 중",
				" 검사중입니다.", true);

	}
	
	
	public String cookItem() {

		/////////////////////////

		// 재료에서 원문장 데이타만 추출
		ArrayList<String> oriList = Util.AdivListEven(uncookedList);
		// 재료에서 가공되지 않는 대치어만 추출
		ArrayList<String> uncookedchangedList = Util.AdivListOdd(uncookedList);

		/* 원문장과 대치어는 순서쌍이 됨 */
		// 다이아로그 창 생성 후 사용자가 값 입력
		Dialog
				.makeDialog(mainActivity.this, text, oriList,
						uncookedchangedList);

		uncookedList.clear();
		uncookedchangedList.clear();
		oriList.clear();

		Log.e("fsf", ">>>>>>>>>>>>>>>start text: " + text);

		return "";

	}

	// 문자수정 일괄 적용
	public static void cookText(String oriText, ArrayList<String> oriList,
			ArrayList<String> cookedList) {

		// EditText에 등록
		// editText.setText(new String(cooking));

		for (int i = 0; i < oriList.size(); i++) {
			Log.e("fsf", "fin oriList[" + i + "]:" + oriList.get(i));
		}
		for (int i = 0; i < cookedList.size(); i++) {
			Log.e("fsf", "fin cookedList[" + i + "]:" + cookedList.get(i));
		}

		String cooking = Util.ChangeStr(oriText, oriList, cookedList);
		Log.e("fsf", "fin oriText: " + oriText);
		Log.e("fsf", "fin cooking: " + cooking);

		// EditText에 등록
		editText.setText(new String(cooking));
		oriList.clear();
		cookedList.clear();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		mMenu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, mMenu);

		// menu.getItem(0).setIcon(R.drawable.icon); // icon 이미지 넣기

		// menu.getItem(1).setTitle("메뉴이름 변환"); // title 변환하기

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu1: // 위에 xml에서 만든 메뉴에서 1번 메뉴 버튼이 눌러졌을때

			Intent makerInfo = new Intent(mainActivity.this,
					makeInfoActivity.class);
			startActivity(makerInfo);

			break;
		}
		return true;
	}

}