package com.telcoware.taxicalluser;

import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class ActComment extends Activity
{
	// System
	private TelephonyManager tManager;
	private String m_strMyPhoneNumber;
	
	// View
	private TextView	vw_txtDriverName;
	private TextView	vw_txtPhoneNumber;
	private RatingBar	vw_Rating;
	private EditText	vw_etxDriverComment;
	private Button		vw_btnCommit;
	
	// Member
	private String m_strTaxiNumber;
	private String m_strPhoneNumber;
	private String m_strLicense;
	private float point;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_comment);
		
		tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		m_strMyPhoneNumber = tManager.getLine1Number().replace("+82", "0");
		
		Intent intent = getIntent();
		
		m_strTaxiNumber = intent.getStringExtra("DRIVER");
		m_strPhoneNumber = intent.getStringExtra("PHONE");
		m_strLicense = intent.getStringExtra("LICENSE");
		point = 0;
		connectView();
		connenctListener();
		setViewData();
	}

	private void connectView()
	{
		vw_txtDriverName = (TextView)findViewById(R.id.txtCommentDriver);
		vw_txtPhoneNumber = (TextView)findViewById(R.id.txtCommentLicense);
		vw_Rating = (RatingBar)findViewById(R.id.rtbCommentRating);
		vw_etxDriverComment = (EditText)findViewById(R.id.etxComment);
		vw_btnCommit = (Button)findViewById(R.id.btnCommentCommit);
		vw_Rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				point = rating;
			}
			
		});
	}

	private void connenctListener()
	{
		vw_btnCommit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ArrayList<String> prmtr = new ArrayList<String>();
				prmtr.add("http://mane.nasse.net/telco/svc_regi.php");
				prmtr.add(m_strMyPhoneNumber);		// 나의 번호
				prmtr.add(m_strLicense);			// 면허번호
				prmtr.add(String.valueOf(point));				// 평가점수
				prmtr.add(vw_etxDriverComment.getText().toString());			// 이용소감
				//Log.d("TAG",vw_Rating+"DD");
				String result = HttpConnect.TagFilter((HttpConnect.postData(prmtr)), "estimation");
				finish();
			}
			
		});
	}
	
	private void setViewData()
	{
		vw_txtDriverName.setText("택시 번호: " + m_strTaxiNumber);
		vw_txtPhoneNumber.setText("전화 번호: " + m_strPhoneNumber);
	}
}
