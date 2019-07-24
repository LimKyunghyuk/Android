package com.testcp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class mainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Button testBtn = (Button) findViewById(R.id.testbtn);
		
		//testBtn.setOnClickListener(new OnClickListener() {

		//	@Override
//			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast = new Toast(mainActivity.this);

				LayoutInflater inflater = getLayoutInflater();
				View vToast = inflater.inflate(R.layout.customtoast, null);

				toast.setView(vToast);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
	//		}

//		});
	}
}
