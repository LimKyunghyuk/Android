package com.telcoware.whoareyou;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.skt.arm.aidl.IArmService;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "MainActivity";

	private static final int SHOW_INTROACTIVITY = Menu.FIRST;
//	private static final int SHOW_MEMBERSHIPACTIVITY = Menu.FIRST + 1;
	private static final int DEV_CONFIG_DIALOG = Menu.FIRST + 2;
	private static final int MENU_DEV_CONFIG = Menu.FIRST + 3;
	
	private IArmService	service;
	private ArmServiceConnection	armCon;
	private String AID = "OA00020744";
	
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	
	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;
	
//	SharedPreferences configPrefs;
//	SharedPreferences devconfigPrefs;
//	SharedPreferences.Editor prefsEditor;
	
	int startedMainActivity;
	
	class ArmServiceConnection implements ServiceConnection {
		public void onServiceConnected (ComponentName name, IBinder boundService)
		{
			// bindService 후 onServiceConnected() 이벤트 발생
			if(service == null)
				service = IArmService.Stub.asInterface((IBinder) boundService);
			try{
				// ARM Service로 AID값 전송
				int res = service.executeArm(AID);
				switch(res){
					case 1:
						// 성공 시
						// Application 정상 구동
					break;
				default:
						// 실패 시
						// 에러메시지 출력 후 Application 종료
						// 에러 코드표 참조 (Appendix)
					break;
				}
			}catch(Exception e){
				releaseService();
				return;
			}
			// ARM Service와의 연결해제
			releaseService();
		}
		
		public void onServiceDisconnected(ComponentName name)
		{
			service = null;
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startedMainActivity = 0;
//        if(!runArmService()) {
//        	return;
//        }
        
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
    	
        startService(new Intent(this, SpamFilterService.class));
        
        ShowIntro();
        
        // Get DevPreferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.dev_config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		case (SHOW_INTROACTIVITY):
			if (startedMainActivity == 0) {
				MakeTab();
			}
			break;
//		case (SHOW_MEMBERSHIPACTIVITY):
//			boolean success = data.getBooleanExtra("success", false);
//		
//			Log.v(logTag, "MEMBERSHIP RETURN : " + resultCode + ", success : " + success);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//return super.onCreateOptionsMenu(menu);
//		int groupId = 0;
//		int menuItemId = MENU_DEV_CONFIG;
//		int menuItemOrder = Menu.NONE;
//		int menuItemText = R.string.dev_config;
//		
//		menu.add(groupId, menuItemId, menuItemOrder, menuItemText);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case (MENU_DEV_CONFIG) : {
			showDialog(DEV_CONFIG_DIALOG);
			return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();	
		
		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}

    
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id) {
		case (DEV_CONFIG_DIALOG) : {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			alertDialog.setTitle(R.string.dev_config);
			
			final CharSequence[] items = {"gaia", "mokavango"};
			int sel = configPrefs.getInt("dev_network", 1);
			alertDialog.setSingleChoiceItems(items, sel, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	prefsEditor.putInt("dev_network", item);
			    	prefsEditor.commit();
			    	Util.log(logApplicationTag, logClassTag, "dev_network = " + item);	
					dialog.cancel();
			    }
			});
			
			return alertDialog.create();
			}
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		switch(id) {
			case (DEV_CONFIG_DIALOG) : {
				AlertDialog alertDialog = (AlertDialog)dialog;
				alertDialog.show();
				
				break;
			}
		}
		super.onPrepareDialog(id, dialog);
	}
	
	boolean runArmService()
	{
		try{
			if(armCon == null){
				// bindService 진행
				armCon = new ArmServiceConnection();
				boolean conRes = bindService(new Intent(IArmService.class.getName()),
											armCon, Context.BIND_AUTO_CREATE);
				if (conRes) return true;
			}
			return true;
		}catch(Exception e){
			releaseService();
			return false;
		}
//		releaseService();
//		return false;
	}
	
	private void releaseService()
	{
		if(armCon != null){
			// unbindService 진행
			unbindService(armCon);
			armCon = null;
			service = null;
		}
	}
	
	private void ShowIntro()
    {
    	Intent intent = new Intent(MainActivity.this, IntroActivity.class);
		startActivityForResult(intent, SHOW_INTROACTIVITY);
    }
		
	private void MakeTab()
	{
		startedMainActivity = 1;
		TabHost mTabHost = getTabHost();
		
//		if (configPrefs.contains("certification") == false)
//		{
//			Intent intent = new Intent(this, MembershipActivity.class);
//			startActivityForResult(intent, SHOW_MEMBERSHIPACTIVITY);
//		}

        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.call_log))
        		.setIndicator(getResources().getString(R.string.call_log), getResources().getDrawable(R.drawable.ic_dialog_dialer))
        		.setContent(new Intent(this, CallLogActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.call_search))
		        .setIndicator(getResources().getString(R.string.call_search), getResources().getDrawable(R.drawable.ic_dialog_map))
		        .setContent(new Intent(this, CallSearchActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.config))
		        .setIndicator(getResources().getString(R.string.config), getResources().getDrawable(R.drawable.ic_dialog_info))
		        .setContent(new Intent(this, ConfigActivity.class)));

		mTabHost.setCurrentTab(0);
          
	}
}