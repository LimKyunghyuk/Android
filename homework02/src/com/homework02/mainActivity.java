package com.homework02;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends Activity {
	/** Called when the activity is first created. */
	
	Button btn1, btn2, btn3;
	
	View.OnClickListener ehander = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.TextView01:
				setContentView(R.id.LinearLayout02);		
				break;
			case R.id.TextView02:
				setContentView(R.id.LinearLayout03);
				break;
			case R.id.TextView03:
				setContentView(R.id.LinearLayout04);
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn1 = (Button)findViewById(R.id.TextView01);
		btn1.setOnClickListener(ehander);
	
		btn2 = (Button)findViewById(R.id.TextView02);
		btn2.setOnClickListener(ehander);
		
		btn3 = (Button)findViewById(R.id.TextView02);
		btn3.setOnClickListener(ehander);
		
	}
}