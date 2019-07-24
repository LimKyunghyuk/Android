package com.telcoware.taxicalluser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActTaxiInfo extends Activity
{
	// System
	private SharedPreferences sharedPref;
	
	private TelephonyManager tManager;
	private String m_strPhoneNumber;
	
	// View
	TextView vw_txtDriverName;
	TextView vw_txtDriverNameT;
	TextView vw_txtTaxiNumber;
	TextView vw_txtTaxiNumberT;
	TextView vw_txtLicense;
	TextView vw_txtLicenseT;
	TextView vw_txtTaxiModel;
	TextView vw_txtTaxiModelT;
	TextView vw_txtTaxiType;
	TextView vw_txtTaxiTypeT;
	
	ImageView vw_imgTaxiDriverPic;
	ImageView vw_barTaxiGrade;
	
	ListView vw_listTaxiInfoComment;
	
	Button vw_btnCall;

	private CTaxiDriver m_driver;
	
	// Database
	DHCallLog dbHelper;
	SQLiteDatabase dbCallLog;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_taxi_info);
		
		sharedPref = getSharedPreferences(getResources().getString(R.string.file_preferences), MODE_WORLD_WRITEABLE);
		
		tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		m_strPhoneNumber = tManager.getLine1Number().replace("+82", "0");
		
		dbHelper = new DHCallLog(this);
		dbCallLog = dbHelper.getWritableDatabase();
		
		connectView();
		
		connectListener();
		
		m_driver = getIntent().getParcelableExtra("taxi");
		
		getDriverInfo();
	}
	
	private void connectView()
	{
		vw_txtDriverName	= (TextView)findViewById(R.id.txtTaxiInfoDriverName);
		vw_txtDriverNameT	= (TextView)findViewById(R.id.txtTaxiInfoDriverNameT);
		vw_txtTaxiNumber	= (TextView)findViewById(R.id.txtTaxiInfoTaxiNumber);
		vw_txtTaxiNumberT	= (TextView)findViewById(R.id.txtTaxiInfoTaxiNumberT);
		vw_txtLicense		= (TextView)findViewById(R.id.txtTaxiInfoLicense);
		vw_txtLicenseT		= (TextView)findViewById(R.id.txtTaxiInfoLicenseT);
		vw_txtTaxiModel		= (TextView)findViewById(R.id.txtTaxiInfoModel);
		vw_txtTaxiModelT	= (TextView)findViewById(R.id.txtTaxiInfoModelT);
		vw_txtTaxiType		= (TextView)findViewById(R.id.txtTaxiInfoType);
		vw_txtTaxiTypeT		= (TextView)findViewById(R.id.txtTaxiInfoTypeT);
		
		vw_imgTaxiDriverPic = (ImageView)findViewById(R.id.imgDriverPic);
		vw_barTaxiGrade		= (ImageView)findViewById(R.id.imgTaxiInfoRate);
		
		vw_listTaxiInfoComment = (ListView)findViewById(R.id.listTaxiInfoComment);
		
		vw_btnCall			= (Button)findViewById(R.id.btnTaxiCall);
		
	}
	
	private void connectListener()
	{
		vw_btnCall.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				new Thread()
				{
					public void run()
					{
						ContentValues value = new ContentValues();
						value.put(DHCallLog.DATE, DateUtils.formatDateTime(getBaseContext(), System.currentTimeMillis(), DateUtils.FORMAT_NUMERIC_DATE));
						value.put(DHCallLog.TIME, DateUtils.formatDateTime(getBaseContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
						value.put(DHCallLog.TAXINUM, m_driver.getTaxiNumber());
						value.put(DHCallLog.PHONENUM, m_driver.getPhone());
						value.put(DHCallLog.LICENSE, m_driver.getLicense());
						dbCallLog.insert(DHCallLog.TABLE_NAME, null, value);
						
						
						ArrayList<String> prmtr = new ArrayList<String>();
						prmtr.add("http://mane.nasse.net/telco/cst_call.php");
						prmtr.add(m_strPhoneNumber);														// 자신의 전화번호
						prmtr.add(Double.toString(((double)sharedPref.getInt("LATITUDE", 0))/1E6));			// 자신의 x좌표
						prmtr.add(Double.toString(((double)sharedPref.getInt("LONGITUDE", 0))/1E6));		// 자신의 y좌표
						prmtr.add(m_driver.getLicense());													// call한 택시의 면허번호
						String result = HttpConnect.TagFilter((HttpConnect.postData(prmtr)), "call");
					}
				}.start(); 
				
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+m_driver.getPhone()));
				startActivity(intent);
			}
		});
	}

	private void getDriverInfo()
	{
		vw_txtDriverName.setText("기사 이름 : ");
		vw_txtDriverNameT.setText(m_driver.getName());
		vw_txtTaxiNumber.setText("택시 번호 : ");
		vw_txtTaxiNumberT.setText(m_driver.getTaxiNumber());
		vw_txtLicense.setText("면허 번호 : ");
		vw_txtLicenseT.setText(m_driver.getLicense());
		vw_txtTaxiModel.setText("차종 : ");
		vw_txtTaxiModelT.setText(m_driver.getTaxiName());   
		
		vw_imgTaxiDriverPic.setImageBitmap(loadImg(m_driver.getImgURL()));
		vw_barTaxiGrade.setImageBitmap(myRatingBar(m_driver.getGrade()));
		
		//vw_listTaxiInfoComment.setAdapter(commentList(m_driver.getLicense()));
		commentList(m_driver.getLicense());
		
		switch(m_driver.getType())
		{
		case CTaxiDriver.TAXI_BUSINESS :
			vw_txtTaxiTypeT.setText("업체명: " + m_driver.getCompany());
			break;
			
		case CTaxiDriver.TAXI_PRIVATE :
			vw_txtTaxiTypeT.setText("개인 택시");
			break;
			
		case CTaxiDriver.TAXI_DELUXE :
			vw_txtTaxiTypeT.setText("모범 택시");
			break;
		}
	}
	
	private void commentList(String license){
		
		String temp;
		ArrayList<String> items;
		
		ArrayList<String> prmtr = new ArrayList<String>();
		ArrayList<String> tagList = new ArrayList<String>();
		ArrayList<EstData> result = new ArrayList<EstData>();
		
		items = new ArrayList<String>();
		prmtr.add("http://mane.nasse.net/telco/svc_get_list.php");
		prmtr.add(license);											// 운전자의 면허번호
		prmtr.add("10");											// 받아올 평가 리스트의 수( 데이터가 없다면 그 이하가 될 수  있음 ) 

		tagList.add("index");		
		tagList.add("phone_number");
		tagList.add("license_number");
		tagList.add("svc_time");
		tagList.add("svc_estimation");
		tagList.add("svc_comment");
														
		result = HttpConnect.estListTagFilter(HttpConnect.postData(prmtr), tagList); 
		
		if(result==null) return;
		
		if(null != result){
			if(0 != result.size()){
				for(int i =0;i<result.size();i++){
					temp = ">" + result.get(i).svc_comment + "("+result.get(i).svc_time+")";
					items.add(temp);
				}
			}else{
				items.add("평가가 없습니다");
			}
		}else{
			items.add("평가가 없습니다");
		}
		
		TaxiAdapter adapter2 = new TaxiAdapter(this, R.layout.list_comment, items);
		vw_listTaxiInfoComment.setAdapter(adapter2);
		prmtr.clear();
		tagList.clear();
		result.clear();
	
	}
	
	
    private Bitmap loadImg(String myImagesURL){

    	Bitmap bm;
    	
    	try {

			URL aURL = new URL(myImagesURL);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();

			return bm;
			
		} catch (IOException e) {

		//	g.setImageResource(R.drawable.icon);
			Log.d("DEBUGTAG", "Remtoe Image Exception");

		}
		
		return null;
		
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
	
    private class TaxiAdapter extends BaseAdapter
	{
		private Context m_context;
		private LayoutInflater inflater;
		private int m_layout;
		private ArrayList<String> m_list;
		
		public TaxiAdapter(Context _context, int _layout, ArrayList<String>_list)
		{
			m_context = _context;
			inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_layout = _layout;
			m_list = _list;
		}
		
		@Override
		public int getCount()
		{
			return m_list.size();
		}

		@Override
		public String getItem(int idx)
		{
			return m_list.get(idx);
		}

		@Override
		public long getItemId(int idx)
		{
			return idx;
		}

		@Override
		public View getView(int idx, View convertView, ViewGroup parent)
		{
			if(convertView == null)
				convertView = inflater.inflate(m_layout, parent, false);
			TextView comments = (TextView)convertView.findViewById(R.id.txtTaxiComment);
			comments.setText(getItem(idx).toString());		
			return convertView;
		}
	}
	
}