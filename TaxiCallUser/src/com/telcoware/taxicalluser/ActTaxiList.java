package com.telcoware.taxicalluser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ActTaxiList extends MapActivity {

	// Layout
	private LinearLayout vg_linMapLayout;

	// View
	// 타이틀 부분
	private TextView vw_txtHeader;

	// Tab Menu
	private ImageView vw_imgMapView;
	private ImageView vw_imgListView;
	private int m_iViewMode;
	private final int MAP_VIEW = 0;
	private final int LIST_VIEW = 1;
	private final int IC_CLIENT = R.drawable.ic_client;

	// Map View
	private MapView vw_mapView;

	// ListView
	private ListView vw_listTaxiList;

	// Map Component
	private MapController m_mapControl;

	private Drawable markerPrivate;
	private Drawable markerBusiness;
	private Drawable markerDeluxe;
	private Drawable markerPrivate_card;
	private Drawable markerBusiness_card;
	private Drawable markerDeluxe_card;
	private Drawable markerClient;

	// Common Data
	private int m_iLatitude;
	private int m_iLongitude;

	private ArrayList<CTaxiDriver> m_listTaxi;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_taxi_list);

		connectView();
		connectListener();

		vw_txtHeader.setText("주변 택시 검색");

		Intent intent = getIntent();

		m_listTaxi = intent.getParcelableArrayListExtra("taxi_list");
		m_iLatitude = intent.getIntExtra("LATITUDE", 0);
		m_iLongitude = intent.getIntExtra("LONGITUDE", 0);

		vg_linMapLayout.setVisibility(View.VISIBLE);
		vw_listTaxiList.setVisibility(View.INVISIBLE);

		// Map View Data Setting
		GeoPoint myPoint = new GeoPoint(m_iLatitude, m_iLongitude);
		m_mapControl.animateTo(myPoint);
		m_mapControl.setZoom(19);

		markerBusiness = getResources().getDrawable(R.drawable.taxi_type0);
		markerBusiness.setBounds(0, 0, markerBusiness.getIntrinsicWidth(),
				markerBusiness.getIntrinsicHeight());

		markerPrivate = getResources().getDrawable(R.drawable.taxi_type1);
		markerPrivate.setBounds(0, 0, markerPrivate.getIntrinsicWidth(),
				markerPrivate.getIntrinsicHeight());

		markerDeluxe = getResources().getDrawable(R.drawable.taxi_type2);
		markerDeluxe.setBounds(0, 0, markerDeluxe.getIntrinsicWidth(),
				markerDeluxe.getIntrinsicHeight());

		markerBusiness_card = getResources().getDrawable(
				R.drawable.taxi_type0_card);
		markerBusiness_card.setBounds(0, 0, markerBusiness_card
				.getIntrinsicWidth(), markerBusiness_card.getIntrinsicHeight());

		markerPrivate_card = getResources().getDrawable(
				R.drawable.taxi_type1_card);
		markerPrivate_card.setBounds(0, 0, markerPrivate_card
				.getIntrinsicWidth(), markerPrivate_card.getIntrinsicHeight());

		markerDeluxe_card = getResources().getDrawable(
				R.drawable.taxi_type2_card);
		markerDeluxe_card.setBounds(0, 0,
				markerDeluxe_card.getIntrinsicWidth(), markerDeluxe_card
						.getIntrinsicHeight());
		markerClient = getResources().getDrawable(IC_CLIENT);
		markerClient.setBounds(0, 0, markerClient.getIntrinsicWidth(),
				markerClient.getIntrinsicHeight());

		/*
		 * TaxiOverlay[] taxiOverlay = new TaxiOverlay[3];
		 * taxiOverlay[CTaxiDriver.TAXI_PRIVATE] = new TaxiOverlay(this,
		 * markerPrivate); taxiOverlay[CTaxiDriver.TAXI_DELUXE] = new
		 * TaxiOverlay(this, markerDeluxe);
		 * taxiOverlay[CTaxiDriver.TAXI_BUSINESS] = new TaxiOverlay(this,
		 * markerBusiness);
		 */

		for (int i = 0; i < m_listTaxi.size(); i++) {
			switch (m_listTaxi.get(i).getType()) {
			case CTaxiDriver.TAXI_PRIVATE:
				if (m_listTaxi.get(i).getPaymentType() == 0) {
					TaxiOverlay taxiOverlay = new TaxiOverlay(this,
							markerPrivate, i);
					GeoPoint point = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi = new OverlayItem(point, "Taxi",
							"Taxidriver");
					taxiOverlay.addOverlay(taxi);
					vw_mapView.getOverlays().add(taxiOverlay);
				} else {
					TaxiOverlay taxiOverlay = new TaxiOverlay(this,
							markerPrivate_card, i);
					GeoPoint point = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi = new OverlayItem(point, "Taxi",
							"Taxidriver");
					taxiOverlay.addOverlay(taxi);
					vw_mapView.getOverlays().add(taxiOverlay);

				}
				break;
			case CTaxiDriver.TAXI_DELUXE:
				if (m_listTaxi.get(i).getPaymentType() == 0) {
					TaxiOverlay taxiOverlay2 = new TaxiOverlay(this,
							markerDeluxe, i);
					GeoPoint point2 = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi2 = new OverlayItem(point2, "Taxi",
							"Taxidriver");
					taxiOverlay2.addOverlay(taxi2);
					vw_mapView.getOverlays().add(taxiOverlay2);
				} else {
					TaxiOverlay taxiOverlay2 = new TaxiOverlay(this,
							markerDeluxe_card, i);
					GeoPoint point2 = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi2 = new OverlayItem(point2, "Taxi",
							"Taxidriver");
					taxiOverlay2.addOverlay(taxi2);
					vw_mapView.getOverlays().add(taxiOverlay2);
				}
				break;
			case CTaxiDriver.TAXI_BUSINESS:
				if (m_listTaxi.get(i).getPaymentType() == 0) {
					TaxiOverlay taxiOverlay3 = new TaxiOverlay(this,
							markerBusiness, i);
					GeoPoint point3 = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi3 = new OverlayItem(point3, "Taxi",
							"Taxidriver");
					taxiOverlay3.addOverlay(taxi3);
					vw_mapView.getOverlays().add(taxiOverlay3);
				} else {
					TaxiOverlay taxiOverlay3 = new TaxiOverlay(this,
							markerBusiness_card, i);
					GeoPoint point3 = new GeoPoint(m_listTaxi.get(i)
							.getLatitude(), m_listTaxi.get(i).getLongitude());
					OverlayItem taxi3 = new OverlayItem(point3, "Taxi",
							"Taxidriver");
					taxiOverlay3.addOverlay(taxi3);
					vw_mapView.getOverlays().add(taxiOverlay3);
				}
				break;
			}
		}
		TaxiOverlay client = new TaxiOverlay(this, markerClient, 100);
		GeoPoint my_loc_point = new GeoPoint(m_iLatitude, m_iLongitude);
		OverlayItem my_loc = new OverlayItem(my_loc_point, "Taxi", "Taxidriver");
		client.addOverlay(my_loc);
		vw_mapView.getOverlays().add(client);
		// ListView Data Setting
		TaxiAdapter adapter = new TaxiAdapter(this, R.layout.list_taxi,
				m_listTaxi);
		vw_listTaxiList.setAdapter(adapter);
		vw_listTaxiList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ActTaxiList.this,
								ActTaxiInfo.class);
						intent.putExtra("taxi", m_listTaxi.get(position));
						startActivity(intent);
					}
				});
	}

	private void connectView() {
		// Linear Layout
		vg_linMapLayout = (LinearLayout) findViewById(R.id.linearTaxiListMapView);

		// Tab Menu
		vw_imgMapView = (ImageView) findViewById(R.id.imgTaxiListMapView);
		vw_imgListView = (ImageView) findViewById(R.id.imgTaxiListListView);

		vw_txtHeader = (TextView) findViewById(R.id.txtTaxiListHeader);

		// Map View
		vw_mapView = (MapView) findViewById(R.id.mapView);

		m_mapControl = vw_mapView.getController();

		// List View
		vw_listTaxiList = (ListView) findViewById(R.id.listTaxiListListView);
	}

	private void connectListener() {
		vw_imgMapView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CL.TouchVibrate(getApplicationContext());
				switch (m_iViewMode) {
				case LIST_VIEW:
					m_iViewMode = MAP_VIEW;
					vw_imgMapView.setImageResource(R.drawable.btn_mapview_on);
					vw_imgListView
							.setImageResource(R.drawable.btn_listview_off);

					vg_linMapLayout.setVisibility(FrameLayout.VISIBLE);
					vw_listTaxiList.setVisibility(FrameLayout.INVISIBLE);
				}
			}
		});
		vw_imgListView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CL.TouchVibrate(getApplicationContext());
				switch (m_iViewMode) {
				case MAP_VIEW:
					m_iViewMode = LIST_VIEW;
					vw_imgMapView.setImageResource(R.drawable.btn_mapview_off);
					vw_imgListView.setImageResource(R.drawable.btn_listview_on);

					vg_linMapLayout.setVisibility(FrameLayout.INVISIBLE);
					vw_listTaxiList.setVisibility(FrameLayout.VISIBLE);
					break;
				}
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class TaxiOverlay extends ItemizedOverlay<OverlayItem> {
		private Context m_context;
		private ArrayList<OverlayItem> arrOverlay;
		private Drawable marker;
		private int taxi_index;

		public TaxiOverlay(Context _context, Drawable _marker, int index) {
			super(_marker);
			marker = _marker;
			m_context = _context;
			arrOverlay = new ArrayList<OverlayItem>();
			taxi_index = index;
		}

		@Override
		protected OverlayItem createItem(int idx) {
			return arrOverlay.get(idx);
		}

		@Override
		public int size() {
			return arrOverlay.size();
		}

		public void addOverlay(OverlayItem _overlay) {
			arrOverlay.add(_overlay);
			populate();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			// Projection projection = mapView.getProjection();
			// Point point = new Point();
			// Paint paint = new Paint();
			// paint.setAntiAlias(true);
			// paint.setTextSize(20);
			// for(int i=0;i<size();i++)
			// {
			// projection.toPixels(createItem(i).getPoint(), point);

			// canvas.drawText(m_listTaxi.get(i).getAddress(), point.x-200,
			// point.y-60, paint);
			// canvas.drawText("거리:"+Float.toString(m_listTaxi.get(i).getDistance())+"km",
			// point.x-200, point.y-40, paint);

			// }
			boundCenterBottom(marker);
		}

		/*
		 * @Override public boolean onTap(GeoPoint point, MapView mapView){
		 * return super.onTap(point, mapView); }
		 */
		@Override
		protected boolean onTap(int index) {
			Log.d("GPS_", "taxi : " + taxi_index);
			if (taxi_index != 100) {
				DialogSimple(taxi_index);
			}
			return true;
		}
	}

	private void DialogSimple(final int taxi_index) {
		
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		

		float temp2, rate2;
		
		rate2 = m_listTaxi.get(taxi_index).getDistance();
		temp2 = (float)(Math.floor((rate2*100))/100.0);
		
		alt_bld.setMessage("거리: "+Float.toString(temp2) + "km\n" + "택시의 정보를 보시겠습니까?").setCancelable(	
		false).setPositiveButton("확인",

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				Intent intent = new Intent(ActTaxiList.this, ActTaxiInfo.class);
				intent.putExtra("taxi", m_listTaxi.get(taxi_index));
				
				startActivity(intent);
				
			}
		}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Action for 'NO' Button
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		
		alert.setTitle(" ");
		int num;
    	float temp, rate;
    	
    	rate = m_listTaxi.get(taxi_index).getGrade();
    	
		// 소수 둘째자리에서 반올림
		temp = (float)(Math.round((rate*10))/10.0);
		num = (int)(temp*2);
		
		switch (num) {

		case 0:
			alert.setIcon(R.drawable.star0);
			break;
		case 1:
			alert.setIcon(R.drawable.star1);
			break;
		case 2:
			alert.setIcon(R.drawable.star2);
			break;
		case 3:
			alert.setIcon(R.drawable.star3);
			break;
		case 4:
			alert.setIcon(R.drawable.star4);
			break;
		case 5:
			alert.setIcon(R.drawable.star5);
			break;
		case 6:
			alert.setIcon(R.drawable.star6);
			break;
		case 7:
			alert.setIcon(R.drawable.star7);
			break;
		case 8:
			alert.setIcon(R.drawable.star8);
			break;
		case 9:
			alert.setIcon(R.drawable.star9);
			break;
		case 10:
			alert.setIcon(R.drawable.star10);
			break;
		}

		alert.show();
	}

	private class TaxiAdapter extends BaseAdapter {
		private Context m_context;
		private LayoutInflater inflater;
		private int m_layout;
		private ArrayList<CTaxiDriver> m_list;

		public TaxiAdapter(Context _context, int _layout,
				ArrayList<CTaxiDriver> _list) {
			m_context = _context;
			inflater = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_layout = _layout;
			m_list = _list;
		}

		@Override
		public int getCount() {
			return m_list.size();
		}

		@Override
		public CTaxiDriver getItem(int idx) {
			return m_list.get(idx);
		}

		@Override
		public long getItemId(int idx) {
			return idx;
		}

		@Override
		public View getView(int idx, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = inflater.inflate(m_layout, parent, false);

			ImageView type = (ImageView) convertView
					.findViewById(R.id.imgTaxiListType);

			if (getItem(idx).getPaymentType() == 0) {
				switch (getItem(idx).getType()) {
				case CTaxiDriver.TAXI_BUSINESS:

					type.setImageResource(R.drawable.taxi_type1);
					break;

				case CTaxiDriver.TAXI_DELUXE:
					type.setImageResource(R.drawable.taxi_type2);
					break;

				case CTaxiDriver.TAXI_PRIVATE:
					type.setImageResource(R.drawable.taxi_type0);
					break;
				}
			} else {
				switch (getItem(idx).getType()) {
				case CTaxiDriver.TAXI_BUSINESS:

					type.setImageResource(R.drawable.taxi_type1_card);
					break;

				case CTaxiDriver.TAXI_DELUXE:
					type.setImageResource(R.drawable.taxi_type2_card);
					break;

				case CTaxiDriver.TAXI_PRIVATE:
					type.setImageResource(R.drawable.taxi_type0_card);
					break;
				}
			}
			TextView distance = (TextView) convertView
					.findViewById(R.id.txtTaxiListDistance);
			String distance2 = "거리: "
					+ (float) ((int) (getItem(idx).getDistance() * 100)) / 100
					+ "Km";

			distance.setText(distance2);
			ImageView star = (ImageView) convertView.findViewById(R.id.star1);
			if (getItem(idx).getGrade() <= 0.2f)
				star.setImageResource(R.drawable.star0);
			else if (getItem(idx).getGrade() <= 0.7f)
				star.setImageResource(R.drawable.star1);
			else if (getItem(idx).getGrade() <= 1.2f)
				star.setImageResource(R.drawable.star2);
			else if (getItem(idx).getGrade() <= 1.7f)
				star.setImageResource(R.drawable.star3);
			else if (getItem(idx).getGrade() <= 2.2f)
				star.setImageResource(R.drawable.star4);
			else if (getItem(idx).getGrade() <= 2.7f)
				star.setImageResource(R.drawable.star5);
			else if (getItem(idx).getGrade() <= 3.2f)
				star.setImageResource(R.drawable.star6);
			else if (getItem(idx).getGrade() <= 3.7f)
				star.setImageResource(R.drawable.star7);
			else if (getItem(idx).getGrade() <= 4.2f)
				star.setImageResource(R.drawable.star8);
			else if (getItem(idx).getGrade() <= 4.7f)
				star.setImageResource(R.drawable.star9);
			else if (getItem(idx).getGrade() <= 5.2f)
				star.setImageResource(R.drawable.star10);

			return convertView;
		}
	}
	
	private Bitmap myRatingBar(float rate) {

	    	int num;
	    	float temp;
			Bitmap bm = null;
			
			// 소수 둘째자리에서 반올림
			temp = (float)(Math.round((rate*10))/10.0);
			num = (int)(temp*2);
			
			switch (num) {

			case 0:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star0);
				break;
			case 1:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star1);
				break;
			case 2:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star2);
				break;
			case 3:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star3);
				break;
			case 4:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star4);
				break;
			case 5:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star5);
				break;
			case 6:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star6);
				break;
			case 7:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star7);
				break;
			case 8:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star8);
				break;
			case 9:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star9);
				break;
			case 10:
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.star10);
				break;
			}
			
			
			return bm;
		}
}