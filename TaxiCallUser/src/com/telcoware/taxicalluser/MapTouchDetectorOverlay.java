package com.telcoware.taxicalluser;


import android.view.MotionEvent;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapTouchDetectorOverlay extends Overlay {
	private SafeHomeMain shm;
	MapTouchDetectorOverlay(SafeHomeMain shm){
		this.shm = shm;
	}
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView){
	//	super.onTouchEvent(e, mapView);
//		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			shm.VisibleButton();
	//		return true;
	//	}
		return false;
	}

}
