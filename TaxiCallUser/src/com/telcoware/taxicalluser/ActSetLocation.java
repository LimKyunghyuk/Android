package com.telcoware.taxicalluser;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.google.android.maps.*;

public class ActSetLocation extends MapActivity
{
	public static final int MODE_SETLOC = 0;
	public static final int MODE_CALLTAXI = 1;
	
	// System
	private Context	m_context;
	private Intent	intent;
	
	// View
	private TextView	vw_txtHeader;
	private ImageView	vw_imgChecker;
	private Button		vw_btnSet;
	
	// Map
	private MapView			mapView;
	private MapController	m_mapController;
	private int				m_iLatitude;
	private int				m_iLongitude;
	
	// MyLocation
	private MyLocationOverlay myLocation;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_location);

		init();
		connectView();
		
		
		vw_txtHeader.setText("내 위치 수정");
		
		m_mapController = mapView.getController();
		m_mapController.setZoom(19);
		
		myLocation = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocation);
		
		mapView.invalidate();
		connectListener();
	}
	
	private void init()
	{
		m_context = this;
		intent = getIntent();
		m_iLatitude = intent.getIntExtra("LATITUDE", 0);
		m_iLongitude = intent.getIntExtra("LONGITUDE", 0);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		myLocation.enableMyLocation();
		myLocation.runOnFirstFix(new Runnable()
		{
			public void run()
			{
				m_mapController.setCenter(myLocation.getMyLocation());
			}
		});
	}
	
	private void connectView()
	{
		vw_txtHeader	= (TextView)findViewById(R.id.txtLocationActHeader);
		vw_imgChecker	= (ImageView)findViewById(R.id.imgLocationChecker);
		vw_btnSet		= (Button)findViewById(R.id.btnLocationSet);
		mapView			= (MapView)findViewById(R.id.mapLocation);
	}
	
	private void connectListener()
	{
		vw_btnSet.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				// Checker에서 GeoPoint 구해오기
				Projection projection = mapView.getProjection();
				GeoPoint point = projection.fromPixels((int)((vw_imgChecker.getRight() + vw_imgChecker.getLeft())/2), (int)((vw_imgChecker.getBottom() + vw_imgChecker.getTop())/2));
				m_iLatitude = point.getLatitudeE6();
				m_iLongitude = point.getLongitudeE6();
				
				// Layout Inflate
				LayoutInflater inflater = LayoutInflater.from(m_context);
				View view = inflater.inflate(R.layout.dlg_comfirm_location, null);
				((TextView)view.findViewById(R.id.txtComfirmLocation)).setText(CL.getAddress(m_iLatitude, m_iLongitude));
				
				AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
				builder.setTitle("나의 위치 지정");
				builder.setView(view);
				
				DialogListener pl = new DialogListener(view);
				builder.setPositiveButton("OK", pl);
				builder.setNegativeButton("Cancel", pl);
				
				AlertDialog dlg = builder.create();
				dlg.show();
			}
		});
	}
	
	private class DialogListener implements DialogInterface.OnClickListener
	{	
		public DialogListener(View inDialogView){}
		
		@Override
		public void onClick(DialogInterface v, int buttonId)
		{
			if(buttonId == DialogInterface.BUTTON1)
			{
				Intent intent = new Intent();
				intent.putExtra("LATITUDE", m_iLatitude);
				intent.putExtra("LONGITUDE", m_iLongitude);
				setResult(RESULT_OK, intent);
				finish();
			}
			else{}
		}
	}
	
	/*
	private class MyItem extends ItemizedOverlay<OverlayItem>
	{
		private Context m_context;
		private Drawable m_marker;
		private ArrayList<OverlayItem> m_listOverlay;
		
		public MyItem(Context _context, Drawable _marker)
		{
			super(_marker);
			m_marker		= _marker;
			m_context		= _context;
			m_listOverlay	= new ArrayList<OverlayItem>();
		}

		@Override
		protected OverlayItem createItem(int idx)
		{
			return m_listOverlay.get(idx);
		}

		@Override
		public int size()
		{
			return m_listOverlay.size();
		}
		
		public void addOverlay(OverlayItem _overlay)
		{
			m_listOverlay.add(_overlay);
			populate();
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow)
		{
			super.draw(canvas, mapView, shadow);		
			boundCenterBottom(m_marker);
		}
		
		@Override
		public boolean onTap(GeoPoint p, MapView v)
		{
			return true;
		}
	}
	*/
	
	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}