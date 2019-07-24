package com.telcoware.taxicalluser;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActSignUp extends Activity
{
	// Service
	ActivityManager actManager;
	
	// Preferences
	SharedPreferences			sharedPref;
	SharedPreferences.Editor	prefEditor;
	WebView wv;
	// View
	private TextView	vw_txtPhoneNumber;
	private Button		vw_btnSubmit;
	
	// Phone State
	TelephonyManager tManager;
	String				m_strPhoneNumber;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		
		init();
		connectView();
		connectListener();
		wv.loadUrl("http://whoru.mokavango.com/help/taxiterm.html");
		tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		m_strPhoneNumber = tManager.getLine1Number().replace("+8210", "010");
		vw_txtPhoneNumber.setText(m_strPhoneNumber);
	}
	
	public void onPause()
	{
		super.onPause();
		
	}

	private void init()
	{
		actManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		
		sharedPref = getSharedPreferences(getResources().getString(R.string.file_preferences), MODE_PRIVATE);
		prefEditor = sharedPref.edit();
	}

	private void connectView()
	{
		wv = (WebView)findViewById(R.id.txtRegisterLicense);
		vw_txtPhoneNumber	= (TextView)findViewById(R.id.txtRegisterPhoneNumber);
		vw_btnSubmit		= (Button)findViewById(R.id.btnRegisterSubmit);
	}

	private void connectListener()
	{
		vw_btnSubmit.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				if(checkAlreadyRegistered())
				{
					prefEditor.putBoolean("IS_REGISTERED", true);
					prefEditor.commit();
					Toast.makeText(getBaseContext(), "이미 가입된 회원입니다.\n프로그램을 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					finish();
				}
				else
				{
					if(registerPhoneNumber())
					{
						prefEditor.putBoolean("IS_REGISTERED", true);
						prefEditor.commit();
						Toast.makeText(getBaseContext(), "회원 가입에 성공했습니다.\n프로그램을 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}
					else
					{
						Toast.makeText(getBaseContext(), "회원 가입에 실패했습니다. 잠시후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
						//actManager.restartPackage(getPackageName());
					}
				}
			}
		});
	}

	private boolean checkAlreadyRegistered()
	{
		ArrayList<String> prmtr = new ArrayList<String>();
		prmtr.add("http://mane.nasse.net/telco/cst_start.php");
		prmtr.add(m_strPhoneNumber); // 자신의 전화번호
		String result = HttpConnect.TagFilter((HttpConnect.postData(prmtr)), "join");
		if(result.equals("nok"))
			return true;
		else
			return false;
	}

	private boolean registerPhoneNumber()
	{
		ArrayList<String> prmtr = new ArrayList<String>();
		prmtr.add("http://mane.nasse.net/telco/cst_join.php");
		prmtr.add(m_strPhoneNumber); // 자신의 전화번호
		prmtr.add("기타"); // 기타
		String result = HttpConnect.TagFilter((HttpConnect.postData(prmtr)), "join");
		if(result.equals("ok"))
			return true;
		else
			return false;
	}
}