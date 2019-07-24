
package com.telcoware.taxicalluser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LocalService extends Service {
	private NotificationManager mNM;
	private static LocationManager locMan;
	private static Location curLocation = null;
	private static LocationListener locationListener;
	private int update_Delay;
	public static final String MY_ACTION = "kr.package.action.MY_ACTION";
	private final String DELAY_EXTRA = "delay_extra";
	SharedPreferences prefs;
	private final String SET_DELAY_KEY = "set_delay_key";
	private final String SERVICE_STARTED_KEY = "service_started_key";
	private final int SERVICE_START = 0;
	private final int SERVICE_STOP = 1;
	static boolean isChanged = false;
	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}

	@Override
	public void onCreate() {	
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		IntentFilter filter = new IntentFilter(MY_ACTION);
		registerReceiver(mReceiver, filter);
		prefs = getSharedPreferences("Safe_Home", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SERVICE_STARTED_KEY, SERVICE_START);
		editor.commit();
		update_Delay = prefs.getInt(SET_DELAY_KEY, 30000);
		locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationListener = new MyLocationManager(this, curLocation,locMan);
		GpsSet();
		if(update_Delay == 0){
			update_Delay = 30000;
		}
		mHandler.sendEmptyMessageDelayed(0, update_Delay); 

		// erviceIntent
		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(R.string.Update_start);
		if(locMan != null)
			locMan.removeUpdates(locationListener);
		unregisterReceiver(mReceiver);
		mHandler.removeMessages(0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SERVICE_STARTED_KEY, SERVICE_STOP);
		editor.commit();
		// Tell the user we stopped.
		Toast.makeText(this, getText(R.string.Update_stop), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Show a notification while this service is running.
	 */
	public void GpsSet() {
		Log.d("GPS_", "reSet my location");
		//Criteria criteria = new Criteria();
		//criteria.setAccuracy(Criteria.ACCURACY_FINE); // 정확도 좋음 , ACCURACY_COARSE 은 정확도 낮음

		//criteria.setAltitudeRequired(false); //고도 사용 유무
		//criteria.setBearingRequired(false); //방위 사용 유무
		//criteria.setCostAllowed(true);	//관련 비용 허용
		//criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setBearingRequired(true);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		//criteria.setCostAllowed(true);	//관련 비용 허용 
	
		String provider = locMan.getBestProvider(criteria, true);
		//String provider = locMan.getAllProviders().get(0);
		//String provider = "gps";
		
		//Log.d("GPS_",provider);
		Log.d("GPS_",provider);
		curLocation = locMan.getLastKnownLocation(provider);		
		// 0초 마다 또는 0미터 이상 이동시 GPS정보를 업데이트하는 리스너 등록
		locMan.requestLocationUpdates(provider, 0, 0, locationListener);
	}
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			GpsSet();
			mHandler.sendEmptyMessageDelayed(0, update_Delay);
		}
	};

	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.Update_start);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ing, text,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, FirstActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.Update_label),
				text, contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.string.Update_start, notification);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (action.equals(MY_ACTION)) {
				Log.d("GPS_", "broadcaset recived!!!");
				update_Delay = intent.getIntExtra(DELAY_EXTRA, 30000);
			}

		}
	};
}
