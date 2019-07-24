package com.telcoware.taxicalluser;

import android.app.*;
import android.os.*;

public class ActIntro extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_intro);
	}
	
	public void onResume()
	{
		super.onResume();
		new CountDownTimer(2000, 2000)
		{
			@Override
			public void onFinish() 
			{
				finish();
			}

			@Override
			public void onTick(long millisUntilFinished){}
		}.start();
	}
}
