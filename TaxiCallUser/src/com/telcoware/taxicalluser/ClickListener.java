package com.telcoware.taxicalluser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ClickListener extends Activity implements OnClickListener {
	public static final String Tag = "SafeHomeMain";
	private final String DELAY_KEY = "delay_key";
	private Context context;
	private final int SET_PHONE_NUM = 0;
	private final int SET_GET_PERIOD = 1;
	private SharedPreferences prefs;
	private SafeHomeMain shm;
	public ClickListener(SafeHomeMain shm) {
		this.context = shm.context;
		this.prefs = shm.prefs;
		this.shm = shm;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.refresh_btn:
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					shm.getLocation();
				}
				
			});
			t.start();
			break;
		case R.id.set_btn:
			new AlertDialog.Builder(context)
            .setTitle("설 정")
            .setItems(R.array.Setting_set_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(which == SET_PHONE_NUM){
                    	shm.showPhoneDialog();
                    }else if(which == SET_GET_PERIOD){
                    	showSetDialog();
                    }
                }
            })
            .show();
			break;

		}
	}
	private void showSetDialog(){			
		int index = 0;
		switch (prefs.getInt(DELAY_KEY, 30000)) {
		case 30000:
			index = 0;
			break;
		case 60000:
			index = 1;
			break;
		case 120000:
			index = 2;
			break;
		case 300000:
			index = 3;
			break;
		}
		new AlertDialog.Builder(context).setTitle(R.string.Setting)
				.setSingleChoiceItems(R.array.Setting_time_array, index,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Log.d(Tag, whichButton + " ");
								if (whichButton == 0) {
									SafeHomeMain.delay = 30000;
								} else if (whichButton == 1) {
									SafeHomeMain.delay = 60000;
								} else if (whichButton == 2) {
									SafeHomeMain.delay = 120000;
								} else if (whichButton == 3) {
									SafeHomeMain.delay = 300000;
								}
							}
						}).setPositiveButton("확인",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SharedPreferences.Editor editor = prefs
										.edit();
								editor.putInt(DELAY_KEY, SafeHomeMain.delay);
								editor.commit();
							}
						}).setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SafeHomeMain.delay = prefs.getInt(DELAY_KEY, 30000);
							}
						}).show();
	}


}
