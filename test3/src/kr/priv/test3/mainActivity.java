package kr.priv.test3;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class mainActivity extends Activity {

	Button btnChk;
	String[] listMenu = { "사과\n사과사과\n사과사과사과사과사과사과사과사과사과", "딸기", "바나나" };
	String[] listMenu2 = { "사과2", "딸2기", "바나2나" };
	
	
	String[] listMenu3 = { "사과2", "딸2기", "바나2나" };
	boolean [] boolArr = {true, false, false, true, false, false, false};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnChk = (Button) findViewById(R.id.Button01);
		btnChk.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				
				ArrayList<String> oriword = new ArrayList<String>();
				oriword.add("사과");
				oriword.add("딸기");
				oriword.add("배");
				oriword.add("바나나");
				oriword.add("포도");
				oriword.add("파인애플");
				oriword.add("키위");
				oriword.add("수박");
				
				
				
				ArrayList<String> str = Util.AdivListEven(oriword);
				for(int i =0;i<str.size();i++)
					Log.d("a",str.get(i));
				
				
				ArrayList<String> str2 = Util.AdivListOdd(oriword);
				for(int i =0;i<str2.size();i++)
					Log.d("a",str2.get(i));
				
				
				
				
				Dialog.makeDialog(mainActivity.this, str,str2);

			}

		});

	}
}