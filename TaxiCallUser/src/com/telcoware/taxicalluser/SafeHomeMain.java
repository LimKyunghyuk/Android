package com.telcoware.taxicalluser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class SafeHomeMain extends MapActivity {
	private final int SETTEXT = 0;
	private final int SETMAP = 1;
	private final int BUTTON = 2;
	private final int NOPERMISSION = 3;
	private final int NONUMBER = 4;
	Handler mHandler;
	LocationManager location = null;
	private MapView mapView = null;
	private MapController mapController;
	private GeoPoint centerGP = null;
	private double currentLat = 0;
	private double currentLng = 0;
	Context context = null;
	private final int IC_MYLOCATION = R.drawable.marker;
	private final int IC_MYLOCATION2 = R.drawable.marker2;
	private static boolean isClicked = true;
	public static int delay; // 갱신 주기
	private static ImageView set_btn;
	private static ImageView refresh_btn;
	public static final String Tag = "SafeHomeMain";
	private final String DELAY_KEY = "delay_key";
	private final String PHONE_KEY = "phone_key";
	private final String LOC_X = "loc_x";
	private final String LOC_Y = "loc_y";
	SharedPreferences prefs;
	private String PhoneNum;
	private String MyPhoneNum;
	private EditText PhoneEdit;
	private TextView adress_view;
	private List<Address> address; 
	private int loc_x;
	private int loc_y;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences("Safe_Home", MODE_PRIVATE);
		this.context = this;
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		mHandler2.sendEmptyMessageDelayed(BUTTON, 6000);
		mHandler = new Handler();
		delay = prefs.getInt(DELAY_KEY, 30000);

		registerEventListener();
	//	showPhoneDialog();
	}

	public void showPhoneDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry, null);

		PhoneEdit = (EditText) textEntryView.findViewById(R.id.Phone_edit);
		PhoneNum = prefs.getString(PHONE_KEY, "");
		PhoneEdit.setText(PhoneNum.toString());
		new AlertDialog.Builder(SafeHomeMain.this)
				.setTitle("상대의 전화번호를 입력하세요.").setView(textEntryView)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						if (PhoneEdit.getText().toString() == "") {

						} else {
							PhoneNum = PhoneEdit.getText().toString();
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(PHONE_KEY, PhoneNum.toString());
							editor.commit();
							Thread t = new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									getLocation();
								}
							});
							t.start();
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

	private Runnable getLoc = new Runnable() {
		public void run() {
			Log.d(Tag, "update mylocation");
			// TODO Auto-generated method stub
			getLocation();
			mHandler.postDelayed(getLoc, delay);
		}
	};

	private Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BUTTON:
				set_btn.setVisibility(Button.INVISIBLE);
				refresh_btn.setVisibility(Button.INVISIBLE);
				isClicked = true;
				break;
			case SETMAP:
				setMap();
				break;
			case SETTEXT:
				adress_view.setText(address.get(0).getAddressLine(0).toString()
						.replace(address.get(0).getCountryName(), ""));
				break;
			case NOPERMISSION:
				Toast.makeText(getApplicationContext(), "상대방이 허용하지 않았거나 서비스 사용중이 아닙니다.", Toast.LENGTH_LONG).show();
				break;
			case NONUMBER:
				Toast.makeText(getApplicationContext(), "위치를 확인할 상대방의 번호를 입력 하세요.", Toast.LENGTH_LONG).show();
				break;
			}

		}
	};

	void getLocation() {
		ArrayList<String> resultList = new ArrayList<String>();
		if (!PhoneNum.equals("")) {
		    TelephonyManager tel = (TelephonyManager ) context.getSystemService(Context.TELEPHONY_SERVICE); 
		    MyPhoneNum = tel.getLine1Number();
		//    MyPhoneNum = "1234";
			resultList = HttpConnect.TagFilter(
					HttpConnect.postData(getString(R.string.Get_Location),
							MyPhoneNum.replace("+8210", "010"), String.valueOf(PhoneNum)), LOC_X, LOC_Y);
			if (resultList == null) {
				Log.d(Tag, "network error");
			} else if (resultList.get(0).equals("nok")) {
				Log.d(Tag, "no permission");
				mHandler2.sendEmptyMessage(NOPERMISSION);
			} else {
				currentLat = Double.valueOf(resultList.get(0));
				currentLng = Double.valueOf(resultList.get(1));
				Log.d(Tag, "location ok");
				mHandler2.sendEmptyMessage(SETMAP);
			}
		}else{
			mHandler2.sendEmptyMessage(NONUMBER);
		}
	}

	void VisibleButton() {
		if (isClicked) {
			set_btn.setVisibility(Button.VISIBLE);
			refresh_btn.setVisibility(Button.VISIBLE);
			mHandler2.sendEmptyMessageDelayed(BUTTON, 6000);
			isClicked = false;
		}
	}

	private void registerEventListener() {

		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setClickable(true);
		mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(17);

		adress_view = (TextView) findViewById(R.id.adress_view);
		set_btn = (ImageView) findViewById(R.id.set_btn);
		refresh_btn = (ImageView) findViewById(R.id.refresh_btn);
		set_btn.setAlpha(200);
		refresh_btn.setAlpha(200);
		OnClickListener mClickListener = new ClickListener(this);
		set_btn.setOnClickListener(mClickListener);
		refresh_btn.setOnClickListener(mClickListener);
		mapView.getOverlays().add(new MapTouchDetectorOverlay(this));// 터치 이벤트를
		// 받기 위해
		// 오버레이
		// 삽입
		mapView.getOverlays().add(new MapTouchDetectorOverlay(this));// 하나가 지워지기
		// 때문에
		// 하나더
		// 삽입

	}

	private void setMap() {
		// mapView.setBuiltInZoomControls(true);
		centerGP = new GeoPoint((int) (currentLat * 1E6),
				(int) (currentLng * 1E6));
		// mapController.setCenter(centerGP);
		Drawable marker2 = getResources().getDrawable(IC_MYLOCATION2);
		// Drawable marker=getResources().getDrawable(R.drawable.marker);
		marker2.setBounds(0, 0, marker2.getIntrinsicWidth(), marker2
				.getIntrinsicHeight());
		if (mapView.getOverlays().size() != 2) {
			mapView.getOverlays().remove(mapView.getOverlays().size() - 1);
			mapView.getOverlays().add(
					new MyLocationsOverlay(marker2, loc_x, loc_y));
		}
		// Log.d(Tag, "Marker set completed" + mapView.getOverlays().size());
		mapController.animateTo(centerGP);
		placeMarker((int) (currentLat * 1E6), (int) (currentLng * 1E6),
				IC_MYLOCATION);
		mapView.invalidate();
		Log.d(Tag, "Marker set completed");

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 마커를 지도에 위치
	 * 
	 * @param markerLatitude
	 * @param markerLongitude
	 */
	private void placeMarker(int markerLatitude, int markerLongitude, int id) {
		loc_x = markerLatitude;
		loc_y = markerLongitude;
		Drawable marker = getResources().getDrawable(id);
		// Drawable marker=getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());

		mapView.getOverlays()
				.add(
						new MyLocationsOverlay(marker, markerLatitude,
								markerLongitude));
	}

	/**
	 * 지도위에 레이어 올리는 클래스
	 */

	class MyLocationsOverlay extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;
		private OverlayItem myOverlayItem;
		private GeoPoint myPlace;

		// Point pixPoint = new Point();
		// address = null;
		Paint paint = new Paint();
		Geocoder geocoder = new Geocoder(context);

		public MyLocationsOverlay(Drawable defaultMarker, final int LatitudeE6,
				final int LongitudeE6) {
			super(defaultMarker);

			// TODO Auto-generated constructor stub
			this.marker = defaultMarker;
			// create locations of interest
			myPlace = new GeoPoint(LatitudeE6, LongitudeE6);
			myOverlayItem = new OverlayItem(myPlace, "My Place", "My Place");
			locations.add(myOverlayItem);
			Thread t = new Thread(new Runnable() {
				public void run() {

					try {
						address = geocoder.getFromLocation(LatitudeE6 / 1E6,
								LongitudeE6 / 1E6, 1);
						if (address != null) {
							mHandler2.sendEmptyMessage(SETTEXT);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
			populate();

		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return locations.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return locations.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);

			/*
			 * mapView.getProjection().toPixels(myPlace, pixPoint); if (address
			 * != null) {
			 * canvas.drawText(address.get(0).getAddressLine(0).toString
			 * ().replace(address.get(0).getCountryName(), ""), pixPoint.x - 30,
			 * pixPoint.y + 30, paint); }
			 */

			boundCenterBottom(marker);
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// TODO Auto-generated method stub
		Log.d(Tag, "Pause");
		mHandler.removeCallbacks(getLoc);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(Tag, "Resume");
		// TODO Auto-generated method stub
		PhoneNum = prefs.getString(PHONE_KEY, "");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getLocation();
			}

		});
		t.start();
		mHandler.postDelayed(getLoc, delay);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(Tag, "Destroy");
		mHandler.removeCallbacks(getLoc);
	}
}