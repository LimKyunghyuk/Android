package com.telcoware.taxicalluser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActMain extends Activity {
	/** Called when the activity is first created. */

	// Request Code
	public static final int REQUEST_SET_LOCATION = 0;
	public static final int ACTSIGN_REQ = 1;
	public static final int LOCDIALOG = 0;
	public static final int MSGNULL = 1;
	// Preference;
	SharedPreferences sharedPref;
	SharedPreferences.Editor prefEditor;

	// Loading Dialog
	private LinearLayout vg_linearDlg;
	private TextView vw_txtLoadDlgDesc;

	// View
	private TextView vw_txtActHeader;
	private TextView vw_txtCurLoc;
	private ImageView vw_btnSetLocation;
	private ImageView vw_btnQuickCall;
	private ImageView vw_btnTaxiList;
	private ImageView vw_btnCallLog;
	private ImageView vw_btnSafeHome;

	LocationManager locManager;
	private boolean hasLocation;

	// Location
	private int m_iLatitude;
	private int m_iLongitude;

	// Data
	private ArrayList<CTaxiDriver> m_listTaxi;

	private Button vm_btnMainAppInfo;					///
	
	AlertDialog.Builder dlgBuilder;

	DHCallLog dbHelper;
	SQLiteDatabase dbCallLog;
	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(ActMain.this, IntroActivity.class);
		startActivity(intent);

		setContentView(R.layout.act_main);
		connectService();
		if (!sharedPref.getBoolean("IS_REGISTERED", false)) {
			Intent registerIntent = new Intent(ActMain.this, ActSignUp.class);
			startActivityForResult(registerIntent,ACTSIGN_REQ);
		}
		connectView();
		connectListener();
		initialize();
		mHandler2.sendEmptyMessageDelayed(LOCDIALOG, 6000);
		LoadingThread loding = new LoadingThread(m_Handler);
		loding.start();
		dialog = ProgressDialog.show(ActMain.this, "", "로딩중...", true);
	}

	private Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
		switch(msg.what){	
		case LOCDIALOG:
			if (!dialog.isShowing()) {
				dialog.dismiss();
			}
			Log.d("GPS_", "OK");
			dialog.dismiss();
			if (!hasLocation) {
				new AlertDialog.Builder(ActMain.this).setTitle("MOBILE CALL")
						.setMessage(
								"GPS정보를 받아오는데 실패 하였습니다. 수동으로 자신의 위치를 설정해 주세요.")
						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												ActMain.this,
												ActSetLocation.class);
										intent
												.putExtra("LATITUDE",
														0);
										intent.putExtra("LONGITUDE",
												0);
										startActivityForResult(intent,
												REQUEST_SET_LOCATION);
										// TODO Auto-generated method
										// stub

									}
								}).show();
				// }
			}
			break;
		case MSGNULL:
			Toast.makeText(getApplicationContext(), "주변에 택시가 없습니다.", 0).show();
			break;

		}
		}
	};

	private void initialize() {
		vw_txtActHeader.setText("현재 나의 위치");
		vw_txtCurLoc.setText("위치를 확인하고 있습니다.");
		m_listTaxi = new ArrayList<CTaxiDriver>();

	}

	@Override
	public void onResume() {
		super.onResume();

		// dialog.dismiss();
		
		//vw_btnTaxiList.setClickable(false);
		//vw_btnQuickCall.setClickable(false);
		//vw_btnSetLocation.setClickable(false);
		//vw_btnCallLog.setClickable(false);

		// 현재 위치 받아오기
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(false);

		locManager.requestLocationUpdates(locManager.getBestProvider(criteria,
				true), 0, 0, locListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		prefEditor.commit();
		locManager.removeUpdates(locListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, 1, 0, "설정");
		item.setIcon(android.R.drawable.ic_menu_preferences);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent(ActMain.this, PActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}

	private void connectService() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		sharedPref = getSharedPreferences(getResources().getString(
				R.string.file_preferences), MODE_WORLD_WRITEABLE);
		prefEditor = sharedPref.edit();
	}

	private void connectView() {
		// Loading Dialog
		vg_linearDlg = (LinearLayout) View.inflate(this,
				R.layout.dlg_main_loading, null);
		vw_txtLoadDlgDesc = (TextView) vg_linearDlg
				.findViewById(R.id.txtMainDlgDesc);

		// View
		vw_txtActHeader = (TextView) findViewById(R.id.txtMainActHeader);
		vw_txtCurLoc = (TextView) findViewById(R.id.txtMainCurrentLocation);
		vw_btnSetLocation = (ImageView) findViewById(R.id.btnMainSetLoc);
		vw_btnQuickCall = (ImageView) findViewById(R.id.btnMainQuickCall);
		vw_btnTaxiList = (ImageView) findViewById(R.id.btnMainTaxiMap);
		vw_btnCallLog = (ImageView) findViewById(R.id.btnMainCallLog);
		vw_btnSafeHome = (ImageView) findViewById(R.id.btnMainSafeCall);
		vm_btnMainAppInfo	= (Button)findViewById(R.id.btnMainAppInfo);
	}

	private void connectListener() {
		vw_btnSetLocation.setOnClickListener(m_onClickListener);
		vw_btnQuickCall.setOnClickListener(m_onClickListener);
		vw_btnTaxiList.setOnClickListener(m_onClickListener);
		vw_btnCallLog.setOnClickListener(m_onClickListener);
		vw_btnSafeHome.setOnClickListener(m_onClickListener);
		vm_btnMainAppInfo.setOnClickListener(m_onClickListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_SET_LOCATION:
			if (resultCode == RESULT_OK) {
				m_iLatitude = data.getIntExtra("LATITUDE", m_iLatitude);
				m_iLongitude = data.getIntExtra("LONGITUDE", m_iLongitude);
				vw_txtCurLoc.setText(CL.getAddress(m_iLatitude, m_iLongitude));
				hasLocation=true;
				LoadingThread loding = new LoadingThread(m_Handler);
				loding.start();
				dialog = ProgressDialog.show(ActMain.this, "", "로딩중...", true);
			}
			break;
		case ACTSIGN_REQ:
			if(resultCode == RESULT_OK){
				
			}else{
				finish();
			}
			break;
		}

		// dlgBuilder.show();

	}

	private LocationListener locListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			m_iLatitude = (int) (location.getLatitude() * 1E6);
			m_iLongitude = (int) (location.getLongitude() * 1E6);

			hasLocation = true;
			vw_txtLoadDlgDesc.setText("위치가 확인되었습니다.");
			vw_txtCurLoc.setText(CL.getAddress(m_iLatitude, m_iLongitude));

			locManager.removeUpdates(this);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private Handler m_Handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LoadingThread.LOCATION:
				vw_txtLoadDlgDesc.setText("위치를 확인하고 있습니다....");
				break;

			case LoadingThread.TAXI:
				vw_txtLoadDlgDesc.setText("주변 택시 정보를 얻어오고 있습니다.");
				break;
			case LoadingThread.COMPLETE:
				prefEditor.putInt("LATITUDE", m_iLatitude);
				prefEditor.putInt("LONGITUDE", m_iLongitude);
				vw_txtLoadDlgDesc.setText("데이터 로딩이 완료되었습니다.");
			//	vw_btnTaxiList.setClickable(true);
			//	vw_btnQuickCall.setClickable(true);
			//	vw_btnSetLocation.setClickable(true);
			//	vw_btnCallLog.setClickable(true);

				dialog.dismiss();
			}
		}
	};

	private View.OnClickListener m_onClickListener = new View.OnClickListener() {
		Intent intent;

		@Override
		public void onClick(View v) {
			CL.TouchVibrate(getApplicationContext());
			switch (v.getId()) {
			case R.id.btnMainSetLoc:
				intent = new Intent(ActMain.this, ActSetLocation.class);
				intent.putExtra("LATITUDE", m_iLatitude);
				intent.putExtra("LONGITUDE", m_iLongitude);
				startActivityForResult(intent, REQUEST_SET_LOCATION);
				break;

			case R.id.btnMainQuickCall:
				if (m_listTaxi == null) {
					Toast.makeText(getApplicationContext(), "주변에 택시가 없습니다.", 0).show();
				} else{
					QuickDialog();
				}
				break;

			case R.id.btnMainTaxiMap:
				if (m_listTaxi == null) {
					Toast.makeText(getApplicationContext(), "주변에 택시가 없습니다.", 0).show();
				} else {
					intent = new Intent(ActMain.this, ActTaxiList.class);
					intent.putExtra("LATITUDE", m_iLatitude);
					intent.putExtra("LONGITUDE", m_iLongitude);
					intent.putParcelableArrayListExtra("taxi_list", m_listTaxi);
					startActivity(intent);
				}
				break;

			case R.id.btnMainCallLog:
				intent = new Intent(ActMain.this, ActCallLog.class);
				startActivity(intent);
				break;
			case R.id.btnMainSafeCall:
				intent = new Intent(ActMain.this, FirstActivity.class);
				startActivity(intent);
				break;
				
			case R.id.btnMainAppInfo:							///
				
				intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
			    intent.setData(Uri.parse("http://mane.nasse.net/telco/appInfo.htm"));
			    startActivity(intent);

				break;
			}
		}
	};

	private void setTaxiDistance() {
		float[] distance = new float[3];
		for (int i = 0; i < m_listTaxi.size(); i++) {
			Location.distanceBetween((double) m_iLatitude / 1E6,
					(double) m_iLongitude / 1E6, (double) m_listTaxi.get(i)
							.getLatitude() / 1E6, (double) m_listTaxi.get(i)
							.getLongitude() / 1E6, distance);
			m_listTaxi.get(i).setDistance(distance[0] / 1000);
			m_listTaxi.get(i).setAddress(
					CL.getAddress(m_listTaxi.get(i).getLatitude(), m_listTaxi
							.get(i).getLongitude()));
		}
	}

	class LoadingThread extends Thread {
		public static final int LOCATION = 0;
		public static final int TAXI = 1;
		public static final int COMPLETE = 2;

		private Handler mHandler;

		LoadingThread(Handler _handler) {
			mHandler = _handler;
		}

		public void run() {
			while (!hasLocation) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//Toast.makeText(getApplicationContext(), "탐색", 0).show();
			ArrayList<String> prmtr = new ArrayList<String>();
			ArrayList<String> tagList = new ArrayList<String>();

			prmtr.add("http://mane.nasse.net/telco/cst_texi_list.php");
			prmtr.add(Double.toString((double) m_iLatitude / 1E6));
			prmtr.add(Double.toString((double) m_iLongitude / 1E6));
			prmtr.add("1000");
			prmtr.add("10");

			tagList.add("license_number");
			tagList.add("car_number");
			tagList.add("phone_number");
			tagList.add("name");
			tagList.add("type");
			tagList.add("company");
			tagList.add("model");
			tagList.add("years");
			tagList.add("card"); // /
			tagList.add("class");
			tagList.add("location_x");
			tagList.add("location_y");
			tagList.add("status");
			tagList.add("customer_number");
			tagList.add("img_url"); // /

			m_listTaxi = HttpConnect.ListTagFilter(HttpConnect.postData(prmtr),tagList);
			if (m_listTaxi == null) {
				mHandler2.sendEmptyMessage(MSGNULL);
				dialog.dismiss();
			} else {
				Message msg = Message.obtain();
				msg.what = TAXI;
				mHandler.sendMessage(msg);

				setTaxiDistance();
				if (sharedPref.getBoolean("SORT_DISTANCE", true)) {
					// 거리순으로 오름차순 정렬
					Collections.sort(m_listTaxi, new Comparator<CTaxiDriver>() {
						@Override
						public int compare(CTaxiDriver driver1,
								CTaxiDriver driver2) {
							if (driver1.getDistance() < driver2.getDistance())
								return -1;
							else
								return 1;
						}
					});
				}

				msg = Message.obtain();
				msg.what = COMPLETE;
				mHandler.sendMessage(msg);
			}
		}
	}

	private String getType(int type) {
		String S_type;
		switch (type) {
		case 0:
			S_type = "개인";
			return S_type;
		case 1:
			S_type = "모범";
			return S_type;
		case 2:
			S_type = "회사";
			return S_type;
		}
		return "";
	}

	private void QuickDialog() {
		new AlertDialog.Builder(ActMain.this).setTitle("QuickCall")
				.setMessage(
						"Call 요청을 하시겠습니까?\n거리 : "
								+ (float) ((int) (m_listTaxi.get(0)
										.getDistance() * 100)) / 100 + "Km"
								+ " 종류  : "
								+ getType(m_listTaxi.get(0).getType()))
				.setPositiveButton("네", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						SaveLog();

						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + m_listTaxi.get(0).getPhone()));

						startActivity(intent);
					}
				}).setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method

							}
						}).show();
	}

	private void SaveLog() {
		dbHelper = new DHCallLog(this);
		dbCallLog = dbHelper.getWritableDatabase();

		ContentValues value = new ContentValues();
		value.put(DHCallLog.DATE, DateUtils.formatDateTime(getBaseContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_NUMERIC_DATE));
		value.put(DHCallLog.TIME, DateUtils.formatDateTime(getBaseContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
		value.put(DHCallLog.TAXINUM, m_listTaxi.get(0).getTaxiNumber());
		value.put(DHCallLog.PHONENUM, m_listTaxi.get(0).getPhone());
		value.put(DHCallLog.LICENSE, m_listTaxi.get(0).getLicense());
		dbCallLog.insert(DHCallLog.TABLE_NAME, null, value);

	}

}