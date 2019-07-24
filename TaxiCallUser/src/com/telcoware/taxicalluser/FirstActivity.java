package com.telcoware.taxicalluser;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FirstActivity extends Activity {
	private ImageView Map_view_btn, Sos_btn, SMS_btn, up_on_btn, up_off_btn;
	private SharedPreferences prefs;
	private final String SET_DELAY_KEY = "set_delay_key";
	private final String Tag = "SafeHomeMain";
	public static final String MY_ACTION = "kr.package.action.MY_ACTION";
	private final String DELAY_EXTRA = "delay_extra";
	private int setDelay;
	private final int SERVICE_START = 0;
	private final int SERVICE_STOP = 1;
	private final String SERVICE_STARTED_KEY = "service_started_key";
	private CheckBox setMyLoc_check_btn;
	private String PhoneNum;
	private EditText PhoneEdit;
	private final String SET_PHONE_KEY = "set_phone_key";
	private final String SOS_PHONE = "sos_phone_key";
	private final int SET_DELAY = 0;
	private final int SET_SOS = 1;
	private final String PROTECTOR = "protector";
	private int state;
	private static LocationManager locMan;
	private static LocationListener locationListener;
	private static Location curLocation = null;
	private List<Address> address;
	Geocoder geocoder;

	// Database
	private DHCallLog dbHelper;
	private SQLiteDatabase dbCallLog;
	// ////////////////////SMS//////////////////////////
	Button btn1, btn2;
	EditText edt;
	String phoneNumber;
	String sosNumber;
	String preMsg;
	String inMsg;
	String postMsg;
	String addrMsg;
	String msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.first_activity);
		prefs = getSharedPreferences("Safe_Home", MODE_PRIVATE);
		Sos_btn = (ImageView) findViewById(R.id.SOS_btn);
		SMS_btn = (ImageView) findViewById(R.id.SMS_btn);
		Map_view_btn = (ImageView) findViewById(R.id.Map_view_btn);
		up_on_btn = (ImageView) findViewById(R.id.up_on_btn);
		up_off_btn = (ImageView) findViewById(R.id.up_off_btn);
		// setMyLoc_check_btn = (CheckBox)
		// findViewById(R.id.setMyLoc_check_btn);
		// setMyLoc_check_btn.setOnCheckedChangeListener(mOnCheckedChangeListener);
		prefs.getInt(SERVICE_STARTED_KEY, SERVICE_STOP);
		phoneNumber = prefs.getString(SOS_PHONE, "");
		// if (prefs.getInt(SERVICE_STARTED_KEY, SERVICE_STOP) == SERVICE_START)
		// {
		// setMyLoc_check_btn.setChecked(true);
		// } else {
		// setMyLoc_check_btn.setChecked(false);
		// }
		Map_view_btn.setOnClickListener(mClickListener);
		Sos_btn.setOnClickListener(mClickListener);
		SMS_btn.setOnClickListener(mClickListener);
		up_on_btn.setOnClickListener(mClickListener);
		up_off_btn.setOnClickListener(mClickListener);
		if (SERVICE_START == prefs.getInt(SERVICE_STARTED_KEY, SERVICE_STOP)) {
			up_on_btn.setVisibility(4);
			up_off_btn.setVisibility(0);
		} else {
			up_on_btn.setVisibility(0);
			up_off_btn.setVisibility(4);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SET_DELAY, 0, "위치 전송 주기 변경");
		menu.add(0, SET_SOS, 0, "SMS 수신 번호 등록");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SET_DELAY:
			showSetDialog();
			return true;
		case SET_SOS:
			showSmsDialog();
			return true;
		}
		return false;
	}

	OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			state = prefs.getInt(SERVICE_STARTED_KEY, SERVICE_STOP);
			if (isChecked == false && state == SERVICE_START) {
				Log.d(SafeHomeMain.Tag, "service stop");
				stopService(new Intent(FirstActivity.this, LocalService.class));
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(SERVICE_STARTED_KEY, SERVICE_STOP);
				editor.commit();
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

						String result = HttpConnect.TagFilter((HttpConnect
								.postData(getString(R.string.Del_Protector),
										tel.getLine1Number())), PROTECTOR);
						// String result =
						// HttpConnect.TagFilter((HttpConnect.postData(getString(R.string.Regi_Protector),"01033333")),
						// PROTECTOR);
						if (result.equals("ok")) {
							Log.d(SafeHomeMain.Tag, "ok del phone num");
						} else if (result.equals("nok")) {
							Log.d(SafeHomeMain.Tag, "nok del phone num");
						} else {
							Log.d(SafeHomeMain.Tag, "network error");
						}
					}
				});
				t.start();
			} else if (isChecked == true && state != SERVICE_START) {
				showPhoneDialog();
			}
			// TODO Auto-generated method stub

		}

	};
	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.up_on_btn:
				showPhoneDialog();
				break;
			case R.id.up_off_btn:
				Log.d(SafeHomeMain.Tag, "service stop");
				stopService(new Intent(FirstActivity.this, LocalService.class));
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(SERVICE_STARTED_KEY, SERVICE_STOP);
				editor.commit();
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

						String result = HttpConnect.TagFilter((HttpConnect
								.postData(getString(R.string.Del_Protector),
										tel.getLine1Number())), PROTECTOR);
						// String result =
						// HttpConnect.TagFilter((HttpConnect.postData(getString(R.string.Regi_Protector),"01033333")),
						// PROTECTOR);
						if (result.equals("ok")) {
							Log.d(SafeHomeMain.Tag, "ok del phone num");
						} else if (result.equals("nok")) {
							Log.d(SafeHomeMain.Tag, "nok del phone num");
						} else {
							Log.d(SafeHomeMain.Tag, "network error");
						}
					}
				});
				t.start();
				up_on_btn.setVisibility(0);
				up_off_btn.setVisibility(4);
				break;
			case R.id.Map_view_btn:
				Intent i = new Intent(FirstActivity.this, SafeHomeMain.class);
				startActivity(i);
				break;
			case R.id.SOS_btn:
				if (phoneNumber.equals("")) {
					Toast.makeText(getApplicationContext(),
							"메뉴에서 SMS를 수신할 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
				} else {
					dbHelper = new DHCallLog(FirstActivity.this);
					dbCallLog = dbHelper.getReadableDatabase();
					Cursor cursor3 = dbCallLog.rawQuery("SELECT * FROM "
							+ DHCallLog.TABLE_NAME, null);
					if (cursor3.getCount() != 0) {
						preMsg = "긴급상황/";
						inMsg = "/기사번호";
						postMsg = "/도와주세요";
						GpsSet();
					} else {
						Toast.makeText(getApplicationContext(),
								"택시 로그 기록이 없습니다.", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.SMS_btn:
				if (phoneNumber.equals("")) {
					Toast.makeText(getApplicationContext(),
							"메뉴에서 SMS를 수신할 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
				}else{
				dbHelper = new DHCallLog(FirstActivity.this);
				dbCallLog = dbHelper.getReadableDatabase();
				Cursor cursor2 = dbCallLog.rawQuery("SELECT * FROM "
						+ DHCallLog.TABLE_NAME, null);
				if (cursor2.getCount() != 0) {
					cursor2.moveToLast();
					sosNumber = cursor2.getString(cursor2
							.getColumnIndex(DHCallLog.PHONENUM));
					String taxi_num = cursor2.getString(cursor2
							.getColumnIndex(DHCallLog.TAXINUM));
					cursor2.close();
					msg = "택시 차량번호 : " + taxi_num + ", " + "기사 전화번호 : "
							+ sosNumber;
				
				new AlertDialog.Builder(FirstActivity.this).setTitle(phoneNumber + "에 SMS를 보내 시겠습니까?").setMessage(msg).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
											SmsManager sm = SmsManager.getDefault();
											sm.sendTextMessage(phoneNumber, null, msg, null,
													null);
										
					
								}
								/* User clicked OK so do some stuff */
							
						}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/* User clicked cancel so do some stuff */
							}
						}).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"택시 로그 기록이 없습니다.", Toast.LENGTH_SHORT).show();
			}
				}
				break;
			}
		}
	};

	public void GpsSet() {
		locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.d("GPS_", "reSet my location");
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setBearingRequired(true);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		String provider = locMan.getBestProvider(criteria, true);
		Log.d("GPS_", provider);
		curLocation = locMan.getLastKnownLocation(provider);
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				try {
					preMsg = "긴급상황/";
					inMsg = "/기사번호";
					postMsg = "/도와주세요";
					dbHelper = new DHCallLog(FirstActivity.this);
					dbCallLog = dbHelper.getReadableDatabase();
					Cursor cursor = dbCallLog.rawQuery("SELECT * FROM "
							+ DHCallLog.TABLE_NAME, null);
					if (cursor.getCount() != 0) {
						cursor.moveToLast();

						// 아래 2개의 변수만 연동하면 됨
						sosNumber = cursor.getString(cursor
								.getColumnIndex(DHCallLog.PHONENUM)); // 도움을 요청할
						// 번호

						// cursor.close();
						geocoder = new Geocoder(FirstActivity.this);
						address = geocoder.getFromLocation(location
								.getLatitude(), location.getLongitude(), 1);
						Log.d("GPS_", location.getLatitude() + " "
								+ location.getLongitude());
						if (address != null)
							addrMsg = address.get(0).getAddressLine(0)
									.toString()
									.replace(address.get(0).getCountryName(),
											"");
						msg = preMsg + addrMsg + inMsg + sosNumber + postMsg;
						// ("긴급상황/구로구 구로동 11-2/기사번호 010-222-3333/도와주세요")
						if (phoneNumber != null && !phoneNumber.equals("")) {
							Toast.makeText(getApplicationContext(), "문자전송",
									Toast.LENGTH_SHORT).show();
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_SHORT).show();

							SmsManager sm = SmsManager.getDefault();
							sm.sendTextMessage(phoneNumber, null, msg, null,
									null);
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"택시 로그 기록이 없습니다.", Toast.LENGTH_SHORT).show();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				locMan.removeUpdates(this);
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}
		};
		// 0초 마다 또는 0미터 이상 이동시 GPS정보를 업데이트하는 리스너 등록
		locMan.requestLocationUpdates(provider, 0, 0, locationListener);
	}

	protected void showSmsDialog() {

		// This example shows how to add a custom layout to an AlertDialog
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry2, null);
		edt = (EditText) textEntryView.findViewById(R.id.phoneNum_edit);
		edt.setText(prefs.getString(SOS_PHONE, ""));
		new AlertDialog.Builder(this).setTitle("SMS 수신 번호").setView(
				textEntryView).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						phoneNumber = edt.getText().toString();
						Toast.makeText(getApplicationContext(), phoneNumber,
								Toast.LENGTH_SHORT).show();
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString(SOS_PHONE, phoneNumber);
						editor.commit();
						/* User clicked OK so do some stuff */
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked cancel so do some stuff */
					}
				}).show();
	}

	public void showPhoneDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry, null);

		PhoneEdit = (EditText) textEntryView.findViewById(R.id.Phone_edit);
		PhoneNum = prefs.getString(SET_PHONE_KEY, "");
		PhoneEdit.setText(PhoneNum.toString());
		new AlertDialog.Builder(FirstActivity.this).setTitle(
				"나의 위치를 확인 할 상대의 번호를 입력하세요.").setView(textEntryView)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						if (PhoneEdit.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(),
									"나의 위치를 확인 할 상대의 번호를 입력 해야 합니다.", Toast.LENGTH_SHORT).show();
						} else {
							PhoneNum = PhoneEdit.getText().toString();
							SharedPreferences.Editor editor = prefs.edit();
							editor
									.putString(SET_PHONE_KEY, PhoneNum
											.toString());
							editor.commit();
							Thread t = new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

									String result = HttpConnect
											.TagFilter(
													(HttpConnect
															.postData(
																	getString(R.string.Regi_Protector),
																	tel
																			.getLine1Number(),
																	PhoneNum)),
													PROTECTOR);
									// String result =
									// HttpConnect.TagFilter((HttpConnect.postData(getString(R.string.Regi_Protector),"01033333",PhoneNum)),
									// PROTECTOR);
									if (result.equals("ok")) {
										Log.d(SafeHomeMain.Tag,
												"ok regi phone num");
									} else if (result.equals("nok")) {
										Log.d(SafeHomeMain.Tag,
												"nok regi phone num");
									} else {
										Log
												.d(SafeHomeMain.Tag,
														"network error");
									}
								}
							});
							t.start();
							Log.d(SafeHomeMain.Tag, "service start");
							startService(new Intent(FirstActivity.this,
									LocalService.class));
							setDelay = prefs.getInt(SET_DELAY_KEY, 30000);
							Intent intent = new Intent(MY_ACTION);
							intent.putExtra(DELAY_EXTRA, setDelay);
							sendBroadcast(intent);
							up_on_btn.setVisibility(4);
							up_off_btn.setVisibility(0);
						}
					}
				}).setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked cancel so do some stuff */
							}
						}).show();

	}

	private void showSetDialog() {
		int index = 0;
		switch (prefs.getInt(SET_DELAY_KEY, 30000)) {
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
		new AlertDialog.Builder(this).setTitle(R.string.Setting)
				.setSingleChoiceItems(R.array.Setting_time_array, index,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Log.d(Tag, whichButton + " ");
								if (whichButton == 0) {
									setDelay = 30000;
								} else if (whichButton == 1) {
									setDelay = 60000;
								} else if (whichButton == 2) {
									setDelay = 120000;
								} else if (whichButton == 3) {
									setDelay = 300000;
								}
							}
						}).setPositiveButton("확인",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SharedPreferences.Editor editor = prefs.edit();
								editor.putInt(SET_DELAY_KEY, setDelay);
								editor.commit();
								Intent intent = new Intent(MY_ACTION);
								intent.putExtra(DELAY_EXTRA, setDelay);
								sendBroadcast(intent);
							}
						}).setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								setDelay = prefs.getInt(SET_DELAY_KEY, 30000);
							}
						}).show();
	}
}
