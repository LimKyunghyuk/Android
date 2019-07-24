package com.telcoware.taxicalluser;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public  class MyLocationManager implements LocationListener {

	private static final String Tag = "GPS_";
	private boolean locationChanged = false;
	private Context context;
	private Location curLocation;
	private final String LOCATION = "location";
	private final String JOIN = "join";
	private String phoneNumber;
	private LocationManager locMan;
	MyLocationManager(Context context,Location curLocation,LocationManager locMan){
		this.context = context;
		this.curLocation = curLocation;
		this.locMan = locMan;
	    TelephonyManager tel = (TelephonyManager ) context.getSystemService(Context.TELEPHONY_SERVICE); 
	    phoneNumber = tel.getLine1Number();
	}
	@Override
	public void onStatusChanged(String provider, int status,
			Bundle extras) {
		// TODO Auto-generated method stub
		Log.d(Tag, "onStatusChanged : " + provider + " & status = " + status);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d(Tag, "GPS Enabled : " + provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d(Tag, "GPS disabled : " + provider);
		
	}

	// 업데이트 조건이 발동되면 gps정보를 서버에 전송
	@Override
	public void onLocationChanged(Location location) {
		Log.d(Tag, "GPS LocationChanged : " + location.getLatitude() + ", " + location.getLongitude());
		// TODO Auto-generated method stub
		if (curLocation == null) {
			curLocation = location;
			locationChanged = true;
			// 서버에 위치 전송
		} else if (curLocation.getLatitude() == location.getLatitude()
				&& curLocation.getLongitude() == location
						.getLongitude()) {
			locationChanged = false;
		} else {
			locationChanged = true;
		}
		curLocation = location;
		// 서버에 위치 전송

		if (locationChanged) {
			Thread t1 = new Thread(new Runnable() {
				public void run() {
					String s = "null";
				//	phoneNumber = "01033333";
					s = HttpConnect.TagFilter((HttpConnect
							.postData(context.getResources().getString(R.string.Join),phoneNumber.replace("+8210", "010"),"null", 
									Double.toString(curLocation.getLatitude()),
									Double.toString(curLocation.getLongitude()))),JOIN);
					if(s == null){
						Log.d(Tag,"Network connect failed" + phoneNumber);
					}else if(s.equals("ok")){
						Log.d(Tag,"Location update Complete :" + phoneNumber);
					}else{
						Log.d(Tag,s + "Location update error");
					}
					
				}
			});
			t1.start();
		}
		locMan.removeUpdates(this);
	}


}
