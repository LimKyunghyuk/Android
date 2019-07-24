package com.telcoware.whoareyou;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.telcoware.whoareyou.CallLogDb.CallLogs;
import com.telcoware.whoareyou.PrivateContactDb.PrivateContacts;

public class CallLogDetailActivity extends Activity {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "CallLogDetailActivity";
	
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	
	SharedPreferences configPrefs;
	
	Bundle extras;
	Integer rowId;
	String command;
	String returnResult;
	ImageView callActionImage;
	TextView callDetailNumberText;
	TextView callDetailNameText;
	TextView callDetailSpamKeywordText;
	TextView callDeatilContentText;
	TextView callDetailSpamLevelText;
	ImageView callDetailSpamLevelImage;
	LinearLayout callDetailLinear2;
	TextView callDetailText;
	Spinner callDetailSpinner;
	Button callDetailLevelButton;
	int callDetailSpamRegi;
	int callDetailSpamLevel;
	String callDetailNumber;
	int callDetailType;
	Long callDetailTime;
	String callDetailContent;
	String callDetailName;
	String callDetailSpamKeyword;
	String callDetailTopSpamKeyword;
	ListView callDetailList;
	View callActionView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_log_detail);
		context = getApplicationContext();

		telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			public void onCallStateChanged(int callState, String inComingNumber) {               
            	switch (callState) {
        		case TelephonyManager.CALL_STATE_RINGING:
        			finish();
            	}  
    		}
		};
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    	
		// Get Preferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Get Views
		callDetailNumberText = (TextView)findViewById(R.id.CallDetailNumberText);
		callDetailNameText = (TextView)findViewById(R.id.CallDetailNameText);
		callDetailSpamKeywordText = (TextView)findViewById(R.id.CallDetailSpamKeywordText);
		callDetailSpamLevelImage = (ImageView)findViewById(R.id.CallDetailSpamLevelImage);
		callDetailLevelButton = (Button)findViewById(R.id.CallDetailLevelButton);
		callActionView = (View)findViewById(R.id.CallDetailCallImage);
		callActionView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (callDetailType == 2) {
					Intent sendIntent = new Intent(Intent.ACTION_VIEW);
					sendIntent.putExtra("address", callDetailNumber); 
					sendIntent.setType("vnd.android-dir/mms-sms");
					startActivity(sendIntent);
				}
				else {
					Uri numberUri = Uri.parse("tel:" + callDetailNumber);
					startActivity(new Intent(Intent.ACTION_DIAL, numberUri));
				}
			}
		});
		
		callActionImage = (ImageView)findViewById(R.id.CallDetailCallImage);
		callDetailText = (TextView)findViewById(R.id.CallDetailText);
		callDetailSpinner = (Spinner)findViewById(R.id.CallDetailSpinner);
		callDetailLinear2 = (LinearLayout)findViewById(R.id.CallDetailLinear2);
		callDetailSpamLevelText = (TextView)findViewById(R.id.CallDetailSpamLevelText);
		callDeatilContentText = (TextView)findViewById(R.id.CallDetailContentText);
		

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		updateCallInfo();
		updateMenuList();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
	
	//Internal Implemntation	
	private void updateCallInfo() {
		
		extras = this.getIntent().getExtras();
        rowId = extras.getInt(getResources().getString(R.string.param_rowid));	
        command = extras.getString(getResources().getString(R.string.cmd));
        Util.log(logApplicationTag, logClassTag, "get rowId: " + rowId.toString());
        Util.log(logApplicationTag, logClassTag, "get command: " + command);
        
    	CallLogDbHelper dbHelper =  new CallLogDbHelper(getApplicationContext());
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	Cursor cursor;
    	
        if (command.compareTo(getResources().getString(R.string.cmd_spam_search)) == 0) {
            cursor = db.query(CallLogs.TABLE_NAME, null,
            								"_id = ?", new String[] { rowId.toString() }, null, null, null);
            cursor.moveToFirst();         
    		callDetailNumber = cursor.getString(cursor.getColumnIndex(CallLogs.NUMBER));
    		callDetailType = cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE));
    		cursor.close();
    		
			Intent intent = new Intent(CallLogDetailActivity.this, ProgressActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra(getResources().getString(R.string.cmd), 
							getResources().getString(R.string.cmd_spam_search));
			intent.putExtra(getResources().getString(R.string.return_intent),
							getResources().getString(R.string.return_intent_call_log_detail));
			intent.putExtra(getResources().getString(R.string.param_rowid), rowId);
			intent.putExtra(getResources().getString(R.string.param_number), callDetailNumber);
			intent.putExtra(getResources().getString(R.string.param_type), callDetailType);
			startActivity(intent);        	
        }
        else if (command.compareTo(getResources().getString(R.string.cmd_return)) == 0) {
        	returnResult = extras.getString(getResources().getString(R.string.return_result));
        	if (returnResult.compareTo(getResources().getString(R.string.ok)) != 0) {
				Toast toast = Toast.makeText(this.getApplicationContext(), returnResult, Toast.LENGTH_SHORT);            					
				toast.setGravity(Gravity.BOTTOM, 0, 90);
				toast.show();
        	}	
        }
        else {
        	Util.log(logApplicationTag, logClassTag, "Unknown Command: " + command);
        }
        
        cursor = db.query(CallLogs.TABLE_NAME, null,
        				  "_id = ?", new String[] { rowId.toString() }, null, null, null);
        cursor.moveToFirst();
        
		callDetailNumber = cursor.getString(cursor.getColumnIndex(CallLogs.NUMBER));
		callDetailName = cursor.getString(cursor.getColumnIndex(CallLogs.NAME));
		callDetailSpamKeyword = cursor.getString(cursor.getColumnIndex(CallLogs.SPAM_KEYWORD));
		callDetailTime = cursor.getLong(cursor.getColumnIndex(CallLogs.DATE));
		callDetailContent = cursor.getString(cursor.getColumnIndex(CallLogs.CONTENT));
		callDetailNumberText.setText(callDetailNumber);
		callDetailType = cursor.getInt(cursor.getColumnIndex(CallLogs.TYPE));
		if (callDetailName == null || callDetailName.compareTo(" ") == 0 || 
				callDetailName.compareTo("Unknown") == 0) {
			callDetailNameText.setText(" ");
		}
		else {
			callDetailNameText.setText("(" + callDetailName + ")");
		}	
		callDetailSpamRegi = cursor.getInt(cursor.getColumnIndex(CallLogs.SPAM_REGI));	
		callDetailSpamKeywordText.setText(Util.getSpamKeywordHtml(callDetailSpamKeyword));
		callDetailSpamLevel = cursor.getInt(cursor.getColumnIndex(CallLogs.SPAM_LEVEL));
		if (callDetailSpamLevel < 0) {
			callDetailSpamLevel = 0;
		}
		callDetailSpamLevelImage.setImageDrawable(Util.getSpamLevelImage(context, callDetailSpamLevel));
		callDetailSpamLevelText.setText(Util.getSpamLevelHtml(callDetailSpamLevel));
		
		if (callDetailType == 2) {
			callDeatilContentText.setText(callDetailContent);
			callDeatilContentText.setTextColor(0XFFFF7F00);
			callActionImage.setImageDrawable(getResources().getDrawable(R.drawable.sms1));
		}
		else {
			callDeatilContentText.setText(" ");
			callActionImage.setImageDrawable(getResources().getDrawable(R.drawable.call3));
		}
		
		cursor.close();
		db.close();
	}

	private void updateMenuList() {
		callDetailLinear2.removeAllViews();
		
		if (callDetailSpamRegi == 0) {			
			String regiKeywordList = " ";
			regiKeywordList = configPrefs.getString("regi_keyword_list", getResources().getString(R.string.regi_keyword_list));
			
			TextView textView = new TextView(this);
			textView.setText("키워드 선택");
			textView.setTextSize(20);
			
			//Spinner
			Spinner spinner = new Spinner(this);
			spinner.setLayoutParams(callDetailSpinner.getLayoutParams());

			String[] spinItems = regiKeywordList.split(",");
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinItems);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
					// TODO Auto-generated method stub
					TextView textView = (TextView)parent.getSelectedView();
					String selected_text = textView.getText().toString();
					Util.log(logApplicationTag, logClassTag, "selected_text: " + selected_text);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				} 	
	        });
	        
	        //Button
			Button levelUpButton = new Button(this);
			levelUpButton.setText(R.string.spam_level_up);
			levelUpButton.setLayoutParams(callDetailLevelButton.getLayoutParams());
			levelUpButton.setTypeface(null, Typeface.ITALIC);
			levelUpButton.setTextSize(20);
			levelUpButton.setTextColor(0xFFFF0000);
			levelUpButton.setTag(spinner);
			levelUpButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					// TODO Auto-generated method stub
					Spinner spinner = (Spinner)view.getTag();
					TextView textView = (TextView)spinner.getSelectedView();
					String selected_text = textView.getText().toString();
					Util.log(logApplicationTag, logClassTag, "selected_text: " + selected_text);
					
	    			ContentResolver r = getContentResolver();   			
	    			r.delete(Uri.parse("content://sms"), "address = ? and date = ?",
	    					 new String[] { callDetailNumber, callDetailTime.toString() });
	    			
					//Spam Regi
					Intent intent = new Intent(CallLogDetailActivity.this, ProgressActivity.class);    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra(getResources().getString(R.string.cmd),
									getResources().getString(R.string.cmd_spam_level_up));
					intent.putExtra(getResources().getString(R.string.return_intent),
									getResources().getString(R.string.return_intent_call_log_detail));
					intent.putExtra(getResources().getString(R.string.param_rowid),	rowId);
					intent.putExtra(getResources().getString(R.string.param_number), callDetailNumber);
					intent.putExtra(getResources().getString(R.string.param_keyword), selected_text);
					intent.putExtra(getResources().getString(R.string.param_type), callDetailType);
					startActivity(intent);
				}
			});	
			
			callDetailLinear2.addView(textView);		
			callDetailLinear2.addView(spinner);
			callDetailLinear2.addView(levelUpButton);
		}
		else {
			callDetailLevelButton.setText(R.string.spam_level_down);
			callDetailLevelButton.setTextColor(0xFF0000FF);
			callDetailLevelButton.setOnClickListener(new View.OnClickListener() {
				Intent intent;
				@Override
				public void onClick(View v) {
					
					// TODO Auto-generated method stub
					Util.log(logApplicationTag, logClassTag, "Select Spam Level Down");
					//Spam UnRegi
					intent = new Intent(CallLogDetailActivity.this, ProgressActivity.class);
        			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra(getResources().getString(R.string.cmd),
									getResources().getString(R.string.cmd_spam_level_down));
					intent.putExtra(getResources().getString(R.string.return_intent),
							getResources().getString(R.string.return_intent_call_log_detail));
					intent.putExtra(getResources().getString(R.string.param_rowid), rowId);
					intent.putExtra(getResources().getString(R.string.param_number), callDetailNumber);
					intent.putExtra(getResources().getString(R.string.param_type), callDetailType);
					startActivity(intent);			
				}
			});
			callDetailLinear2.addView(callDetailLevelButton);
		}
		
		//Button
		Button removeButton = new Button(this);
		removeButton.setText(R.string.remove);
		removeButton.setLayoutParams(callDetailLevelButton.getLayoutParams());
		removeButton.setTextSize(20);
	
		removeButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
		    	CallLogDbHelper dbHelper =  new CallLogDbHelper(getApplicationContext());
		    	SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.delete(CallLogs.TABLE_NAME, "_id = ?", new String[] { rowId.toString() });
				db.close();
				finish();
			}
		});	
		callDetailLinear2.addView(removeButton);
		
		if (callDetailType == 2 && callDetailSpamLevel >= 0) {
			//Button
			Button moveButton = new Button(this);
			moveButton.setText(R.string.move_to_inbox);
			moveButton.setLayoutParams(callDetailLevelButton.getLayoutParams());
			moveButton.setTextSize(20);
		
			moveButton.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
//					ContentValues val = new ContentValues();
//			        val.put("address", callDetailNumber);
//			        val.put("date", callDetailTime);
//			        val.put("read", 1);
//			        val.put("status", -1);
//			        val.put("type", 1); /* 1: INBOX, 2: SENT */
//			        val.put("body", callDetailContent);
//			        Uri inserted = getContentResolver().insert(Uri.parse("content://sms"), val);
//			        if (inserted == null) {
//			        	return ;
//			        }
//			        
//		        	CallLogDbHelper dbHelper =  new CallLogDbHelper(getApplicationContext());
//			    	SQLiteDatabase db = dbHelper.getWritableDatabase();
//			        db.delete(CallLogs.TABLE_NAME, "_id = ?", new String[] { rowId.toString() });
//			        db.close();
//			        finish();
					
					PrivateContactDbHelper dbHelper = new PrivateContactDbHelper(getApplicationContext());
					
					try
					{
						SQLiteDatabase db = dbHelper.getWritableDatabase();
						long rowId = -1;
						
						ContentValues values = new ContentValues();
			    		values.put(PrivateContacts.NUMBER, callDetailNumber);
			    		values.put(PrivateContacts.NAME, "my number1");
						
			    		rowId = db.insert(CallLogs.TABLE_NAME, null, values);
			    		
			    		if (rowId >= 0) {
			    			Util.log(logApplicationTag, logClassTag, "Insert Private Contact" + ", insertNum = " + callDetailNumber + ", rowId = " + rowId);
			    		}
			    		else {
			    			Util.log(logApplicationTag, logClassTag, "Fail to insert Private Contact" + ", insertNum = " + callDetailNumber);
			    		}
					}
					catch(SQLiteException ex) {
						Util.log(logApplicationTag, logClassTag, "SQLite Exception : " + ex.getMessage());
					}
					
				}
			});	
			callDetailLinear2.addView(moveButton);		
		}
	}
}
