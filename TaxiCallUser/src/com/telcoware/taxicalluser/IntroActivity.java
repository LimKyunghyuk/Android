package com.telcoware.taxicalluser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setTheme(R.style.right);
//		setTheme(R.style.ThemeGlow);
//		setContentView(R.layout.style_samples);		
		
		setContentView(new IntroView(this));
		
		Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
         // Do Something
         public void run()
         {     
        	 	setResult(RESULT_OK, null);
                finish();         
         }
        }, 1000);
        
		
	}
}
