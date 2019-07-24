package com.telcoware.whoareyou;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

public class ConfigActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "ConfigActivity";
	
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;

	SharedPreferences configPrefs;
	SharedPreferences devconfigPrefs;
	SharedPreferences.Editor prefsEditor;
	LinearLayout easterEgg;
	int easterEggTouchCount;
	
	ToggleButton serviceOnoffToggle;
	Spinner networkTypeSpinner;
	Spinner smsFilterLevelSpinner;
	Button calllogResetButton;		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		
		telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			public void onCallStateChanged(int callState, String inComingNumber) {               
            	switch (callState) {
        		case TelephonyManager.CALL_STATE_RINGING:
        			finish();
            	}  
    		}
		};
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        		
		// Get Preferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		serviceOnoffToggle = (ToggleButton)findViewById(R.id.ServiceOnoffToggle);
		serviceOnoffToggle.setChecked(configPrefs.getBoolean("service", true));
		serviceOnoffToggle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				ToggleButton serviceOnoffToggle = (ToggleButton)findViewById(R.id.ServiceOnoffToggle);
				if (serviceOnoffToggle.isChecked()) {
					prefsEditor.putBoolean("service", true);
					prefsEditor.commit();
					Util.log(logApplicationTag, logClassTag, "service is true");
				}
				else {
					prefsEditor.putBoolean("service", false);
					prefsEditor.commit();
					Util.log(logApplicationTag, logClassTag, "service is false");
				}
			}
		});
		
		networkTypeSpinner = (Spinner)findViewById(R.id.NetworkTypeSpinner);
		networkTypeSpinner.setSelection(configPrefs.getInt("network", 0));
		networkTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				
				// TODO Auto-generated method stub
				prefsEditor.putInt("network", position);
				prefsEditor.commit();
				Util.log(logApplicationTag, logClassTag, "network = " + position);	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		calllogResetButton = (Button)findViewById(R.id.callLogResetButton);		
		calllogResetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Alert Dialog
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfigActivity.this);
				alertDialog.setTitle(android.R.string.dialog_alert_title);
				alertDialog.setMessage(R.string.are_you_sure);
				// Yes Button
				alertDialog.setPositiveButton(getResources().getString(android.R.string.yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							CallLogDbHelper dbHelper = new CallLogDbHelper(getApplicationContext());					
							SQLiteDatabase db = dbHelper.getWritableDatabase();
							db.delete(CallLogs.TABLE_NAME, null, null);
							db.close();
							Util.log(logApplicationTag, logClassTag, "Deleted Call Logs Table");
						}
					});
				// No Button
				alertDialog.setNegativeButton(getResources().getString(android.R.string.no),
					new DialogInterface.OnClickListener() {							
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
				alertDialog.setCancelable(true);
				alertDialog.show();
			}
		});
		
		smsFilterLevelSpinner = (Spinner)findViewById(R.id.SmsFilterLevelSpinner);
		int position = configPrefs.getInt("sms_filter_level", 2) - 1;
		smsFilterLevelSpinner.setSelection(position);
		smsFilterLevelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int smsFilterLevel = position + 1;
				prefsEditor.putInt("sms_filter_level", smsFilterLevel);
				prefsEditor.commit();
				Util.log(logApplicationTag, logClassTag, "sms_filter_level=" + smsFilterLevel);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TextView helpText = (TextView)findViewById(R.id.HelpLink);
		TextView smsFilterText = (TextView)findViewById(R.id.SmsFilterIndexText);
		
		devconfigPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
		if (devconfigPrefs.getInt("dev_network", 1) == 0)
		{	
			helpText.setText(Html.fromHtml("<a href=" + getString(R.string.help_site) +">바로가기</a>"));
			smsFilterText.setText(Html.fromHtml("<a href=" + getString(R.string.sms_filter_index_term_site) +">SMS 필터지수</a>"));
		} else
		{
			helpText.setText(Html.fromHtml("<a href=" + getString(R.string.help_site_outter) +">바로가기</a>"));
			smsFilterText.setText(Html.fromHtml("<a href=" + getString(R.string.sms_filter_index_term_site_outter) +">SMS 필터지수</a>"));
		}
		
		helpText.setMovementMethod(LinkMovementMethod.getInstance());
		smsFilterText.setMovementMethod(LinkMovementMethod.getInstance());
		
		//Easter Egg
		easterEggTouchCount = 0;
		easterEgg = (LinearLayout)findViewById(R.id.EasterEgg);
		easterEgg.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				easterEggTouchCount++;
				if (easterEggTouchCount >= 7) {
					Intent intent = new Intent(ConfigActivity.this, EasterEggActivity.class);
					startActivity(intent); 
				}
				return false;
			}
			
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		easterEggTouchCount = 0;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
}
