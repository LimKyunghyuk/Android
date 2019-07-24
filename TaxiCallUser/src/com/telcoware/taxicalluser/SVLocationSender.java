package com.telcoware.taxicalluser;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.widget.*;

public class SVLocationSender extends Service
{
	LocationManager locManager;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent arg0)
	{
		return externalInterface;
	}
	
	private SenderControl.Stub externalInterface = new SenderControl.Stub()
	{
		@Override
		public void startSend() throws RemoteException
		{
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 1000, locationListener);
		}

		@Override
		public void endSend() throws RemoteException
		{
			locManager.removeUpdates(locationListener);
		}
	};
	
	private LocationListener locationListener = new LocationListener()
	{
		@Override
		public void onLocationChanged(Location location)
		{
			sendLocation(location.getLatitude(), location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider){}

		@Override
		public void onProviderEnabled(String provider){}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){}
	};

	public void sendLocation(double latitude, double longitude)
	{
		Toast toast = new Toast(this);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setText("위치를 등록중입니다.");
		toast.show();
	}
}