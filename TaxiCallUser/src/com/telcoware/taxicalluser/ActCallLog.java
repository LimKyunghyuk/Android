package com.telcoware.taxicalluser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActCallLog extends Activity
{
	// Database
	DHCallLog dbHelper;
	SQLiteDatabase dbCallLog;
	
	// Data
	ArrayList<CCallLog> m_listCallLog;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_call_log);
		
		dbHelper = new DHCallLog(this);
		dbCallLog = dbHelper.getReadableDatabase();
		
		m_listCallLog = new ArrayList<CCallLog>();
		
		Cursor cursor = dbCallLog.rawQuery("SELECT * FROM " + DHCallLog.TABLE_NAME, null);
		
		CCallLog cLog;
		
		if(0==cursor.getCount()){
			TextView txt = (TextView)findViewById(R.id.txtCallogEmpty);
			txt.setText("택시 이용 내역이 존재하지 않습니다.");
			txt.setTextSize(15);
		
		}else{
			while(cursor.moveToNext())
			{
				cLog = new CCallLog();
				cLog.setDate(cursor.getString(cursor.getColumnIndex(DHCallLog.DATE)));
				cLog.setTime(cursor.getString(cursor.getColumnIndex(DHCallLog.TIME)));
				cLog.setTaxiNumber(cursor.getString(cursor.getColumnIndex(DHCallLog.TAXINUM)));
				cLog.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DHCallLog.PHONENUM)));
				cLog.setLicense(cursor.getString(cursor.getColumnIndex(DHCallLog.LICENSE)));
				m_listCallLog.add(cLog);
				
			}
			CallLogAdapter adapter = new CallLogAdapter(this, R.layout.list_call_log, m_listCallLog);
			ListView listView;
			listView = (ListView)findViewById(R.id.listCallLog);
			listView.setAdapter(adapter);
		}
		
	}
	
	private class CCallLog
	{
		private String m_strDate;
		private String m_strTime;
		private String m_strTaxiNumber;
		private String m_strPhoneNumber;
		private String m_strLicense;
		
		public CCallLog(){}

		public void setDate(String m_strDate) {
			this.m_strDate = m_strDate;
		}

		public String getDate() {
			return m_strDate;
		}

		public void setTime(String m_strTime) {
			this.m_strTime = m_strTime;
		}

		public String getTime() {
			return m_strTime;
		}

		public void setTaxiNumber(String m_strTaxiNumber) {
			this.m_strTaxiNumber = m_strTaxiNumber;
		}

		public String getTaxiNumber() {
			return m_strTaxiNumber;
		}

		public void setPhoneNumber(String m_strPhoneNumber) {
			this.m_strPhoneNumber = m_strPhoneNumber;
		}

		public String getPhoneNumber() {
			return m_strPhoneNumber;
		}

		public void setLicense(String m_strLicense) {
			this.m_strLicense = m_strLicense;
		}

		public String getLicense() {
			return m_strLicense;
		}
	}
	
	private class CallLogAdapter extends BaseAdapter
	{
		Context m_context;
		int m_layout;
		ArrayList<CCallLog> m_list;
		LayoutInflater inflater;
		
		public CallLogAdapter(Context _context, int _layout, ArrayList<CCallLog> _list)
		{
			m_context = _context;
			m_layout = _layout;
			m_list = _list;
			
			inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount()
		{
			return m_list.size();
		}

		@Override
		public CCallLog getItem(int position)
		{
			return m_list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final CCallLog log = getItem(position);
			
			if(convertView == null)
				convertView = inflater.inflate(m_layout, parent, false);
			
			TextView date = (TextView)convertView.findViewById(R.id.txtCallLogDate);
			TextView time = (TextView)convertView.findViewById(R.id.txtCallLogTime);
			TextView taxi = (TextView)convertView.findViewById(R.id.txtCallLogTaxiNumber);
			ImageView call = (ImageView)convertView.findViewById(R.id.imgCallLogCall);
			Button comment = (Button)convertView.findViewById(R.id.btnCallLogComment);
			
			date.setText("호출 날짜 : " + log.getDate());
			time.setText("호출 시간 : " + log.getTime());
			taxi.setText("택시 번호 : " + log.getTaxiNumber());
			call.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + log.getPhoneNumber()));
					startActivity(intent);
				}
				
			});
			
			comment.setText("평가하기");
			comment.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(ActCallLog.this, ActComment.class);
					intent.putExtra("DRIVER", log.getTaxiNumber());
					intent.putExtra("PHONE", log.getPhoneNumber());
					intent.putExtra("LICENSE", log.getLicense());
					startActivity(intent);
				}
				
			});
			
			return convertView;
		}		
	}
}