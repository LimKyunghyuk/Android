package kr.priv.woorisms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class makeInfoActivity extends Activity {

	private TextView makeInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeinfo);
		
		makeInfo = (TextView)findViewById(R.id.txt_makeinfo);
		makeInfo.setText("우리문자 1.0\n중앙대학교 컴퓨터공학부\n임경혁\nHttp://priv.kr");
		
			
	}

}
