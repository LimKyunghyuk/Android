package com.telcoware.whoareyou;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.telcoware.whoareyou.CallLogDb.CallLogs;

import java.util.Date;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Locale;
import android.widget.TextView;
import android.provider.CallLog;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import android.app.AlarmManager;
import android.app.PendingIntent;

public class SpamFilterService extends Service {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "SpamFilterService";
	
	Context context;
	TelephonyManager telManager;
	PhoneStateListener phoneStateListener;
	
	CallLogDbHelper callLogDbHelper;
	SQLiteDatabase callLogDb;
	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;
	
	long insertRowId = -1;
	int lastCallState = TelephonyManager.CALL_STATE_IDLE;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this.getApplicationContext();
		
		// Set CallLogDB
		try {
			// Create CallLog Db
			callLogDbHelper = new CallLogDbHelper(this.getApplicationContext());					
			callLogDb = callLogDbHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Get Preferences
		try {
			configPrefs = getSharedPreferences(getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (configPrefs.contains("service") == false) {				
			prefsEditor.putBoolean("service", true);
			prefsEditor.commit();
			Util.log(logApplicationTag, logClassTag,  "service  = true");
		}
		if (configPrefs.contains("network") == false) {
			prefsEditor.putInt("network", 0);
			prefsEditor.commit();
			Util.log(logApplicationTag, logClassTag,  "network = 0");
		}
		if (configPrefs.contains("certification") == false) {
			prefsEditor.putBoolean("certification", false);
			prefsEditor.commit();
			Util.log(logApplicationTag, logClassTag,  "certification = false");
		}
		if (configPrefs.contains("sms_filter_level") == false) {
			prefsEditor.putInt("sms_filter_level", 2);
			prefsEditor.commit();
			Util.log(logApplicationTag, logClassTag,  "sms_filter_level = 2");
		}
		if (configPrefs.contains("regi_keyword_list") == false) {
			prefsEditor.putString("regi_keyword_list", getResources().getString(R.string.regi_keyword_list));
			prefsEditor.commit();
			Util.log(logApplicationTag, logClassTag,  "regi_keyword_list = " + getResources().getString(R.string.regi_keyword_list));	
		}
		
		// Set PhoneStateListener
        telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
        	public void onCallStateChanged(int callState, final String inComingNumber) {        		
                switch (callState) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (insertRowId >= 0 && lastCallState == TelephonyManager.CALL_STATE_RINGING) {
                    	ContentValues callLogTypeValue = new ContentValues();
                    	callLogTypeValue.put(CallLogs.TYPE, 1); //Missed
                    	callLogDb.update(CallLogs.TABLE_NAME, callLogTypeValue,
                    		"_id = ?", new String[] { new Long(insertRowId).toString() });
                    	Util.log(logApplicationTag, logClassTag,  "Update DB Call Log Type, rowId = " + insertRowId + " Type = " + 1);
                    }
                    insertRowId = -1;
                    lastCallState = TelephonyManager.CALL_STATE_IDLE;
                	break;
            	case TelephonyManager.CALL_STATE_OFFHOOK:
                    insertRowId = -1;
                    lastCallState = TelephonyManager.CALL_STATE_OFFHOOK;                    
            		break;
            	case TelephonyManager.CALL_STATE_RINGING:      			
            		insertRowId = -1;
            		lastCallState = TelephonyManager.CALL_STATE_RINGING; 
            		//Service Available Check
            		if (configPrefs.getBoolean("service", false) == false) {
            			Util.log(logApplicationTag, logClassTag,  "service is not available");
            			break;
            		}
            		
            		//Certification Available Check
            		if (configPrefs.getBoolean("certification", false) == false) {
            			Util.log(logApplicationTag, logClassTag,  "certification is not available");
            			break;
            		}
            		
            		//Search InComingNumber in Contract
            		if (Util.ContactExist(context, inComingNumber) >= 0) {
            			Util.log(logApplicationTag, logClassTag,  inComingNumber + "is in Contracts");
            			
            			Toast introText = Toast.makeText(getApplicationContext(),
								"있는 번호", Toast.LENGTH_SHORT);
						introText.setGravity(Gravity.BOTTOM, 0, 90);
						introText.show();
					
						int callCnt = 0, smsCnt = 0;
						int msgType = 0;
						long gap = 0;
						String callLogNum = "";
						String toast_down_str = "";
						// Get Today
						Date today = new Date ();
						Date latelyDate = today;
						Calendar cal = Calendar.getInstance ( );
						cal.setTime ( today ); 
						
						//Get CallLog
						Cursor c = getContentResolver().query(
								CallLog.Calls.CONTENT_URI, null, null, null,
								null);
						
						if(null != c && c.moveToFirst()){
						
							while (c.isAfterLast() == false) {
								
								callLogNum = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
							
								// if callLog include callNumber 
								if (inComingNumber.equals(callLogNum)) {
									gap = (today.getTime() - latelyDate.getTime())/(1000*60*60*24); 
									
									// for recently 30days
									if((int)gap < 30){
										// sms or mms = 1, call = others( guess call time.. )
										msgType = c.getInt(CallLog.Calls.MISSED_TYPE);
										
										//callLog count
										if( 1 == msgType){ 
											smsCnt++;	
										}else{
											callCnt++;
										}
									
										// recently date
										latelyDate  = new Date(c.getLong(c
												.getColumnIndex(CallLog.Calls.DATE)));
									}
								}
								c.moveToNext();
							}
	
							SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy MM dd HH mm", Locale.KOREA );
							SimpleDateFormat outputDate = new SimpleDateFormat ( "yyyy.MM.dd HH:mm", Locale.KOREA );
							
							String curTime = "N N N N N";
							String output_latelyDate = outputDate.format(latelyDate);
							
							curTime = formatter.format(latelyDate);
							StringTokenizer tok = new StringTokenizer(curTime, " ");
							
							String[] arr = new String[3];
							arr[0] = tok.nextToken();	// Y
							arr[1] = tok.nextToken();	// M
							arr[2] = tok.nextToken();	// D
						
							// set latelyDate on D-day
							Calendar cal2 = Calendar.getInstance ( );
							cal2.set( Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2])); 
	
							int dDayCnt = 0;
							long dTimCnt = 0;
							
							// if D-Day
							if(!cal2.after ( cal )){
								while ( !cal2.after ( cal ) )
								{
									dDayCnt++;
									cal2.add ( Calendar.DATE, 1 );
								}	
							// if D-Hour
							}else{
								dTimCnt = ( today.getTime() - latelyDate.getTime())/(60 * 60 * 1000);
							}
							
							String telCntStr = String.valueOf(callCnt);
							String smsCntStr = String.valueOf(smsCnt);
							String dDayCntStr = String.valueOf(dDayCnt);
							String dHourCntStr = Long.toString(dTimCnt);
							
							// output Toast view latelyDate & D-day
							Toast viewToast = new Toast(SpamFilterService.this);
							LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							
							View vToast = inflater.inflate(R.layout.customtoast, null);
							
							TextView toast_up = (TextView)vToast.findViewById(R.id.toast_TextView_upText);
							toast_up.setText("최근 연락한 날짜");
							
							TextView toast_down = (TextView)vToast.findViewById(R.id.toast_TextView_downText);
							
							if( 0 != dDayCnt ){
								toast_down_str = output_latelyDate + " ( D-Day: "+dDayCntStr+" )";
							} else {
								toast_down_str = output_latelyDate + " ( D-Hour: "+dHourCntStr+" )";
							}
							
							toast_down.setText(toast_down_str);
							viewToast.setView(vToast);
							viewToast.setDuration(Toast.LENGTH_LONG);
							viewToast.show();
						
							// output Toast view call & SMS counter
							Toast viewToast2 = new Toast(SpamFilterService.this);
							LayoutInflater inflater2 = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View vToast2 = inflater2.inflate(R.layout.customtoast, null);
							
							TextView toast_sms = (TextView)vToast2.findViewById(R.id.toast_TextView_upText);
							toast_sms.setText("최근 30일간 연락한 횟수");
							
							TextView toast_call = (TextView)vToast2.findViewById(R.id.toast_TextView_downText);
							toast_call.setText("전화 : " + telCntStr + "건, " + "문자 : " + smsCntStr + "건");
							
							viewToast2.setView(vToast2);
							viewToast2.setDuration(Toast.LENGTH_LONG);
							viewToast2.show();
						}

            			break;
            		}
            		//Check Network Status          		
            		if (Util.isAvailableNetwork(context, configPrefs.getInt("network", 0)) < 0) {
            			break;
            		}
            		// Basic Call Log DB Insert
            		ContentValues callLogValues = new ContentValues();
            		callLogValues.put(CallLogs.NUMBER, inComingNumber);
            		callLogValues.put(CallLogs.NAME, getResources().getString(R.string.unknown));
            		callLogValues.put(CallLogs.SPAM_LEVEL, -1); //Unknown
            		callLogValues.put(CallLogs.SPAM_KEYWORD, getResources().getString(R.string.unknown));
            		callLogValues.put(CallLogs.SPAM_REGI, 0); //UnRegi
            		callLogValues.put(CallLogs.DATE, System.currentTimeMillis());
            		callLogValues.put(CallLogs.TYPE, 0); //Incoming
            		insertRowId = callLogDb.insert(CallLogs.TABLE_NAME, null, callLogValues);
            		if (insertRowId < 0) {
            			Util.log(logApplicationTag, logClassTag,  "DB Insert Fail");
            			break;
            		}
            		Util.log(logApplicationTag, logClassTag,  "Insert Call Log, rowId = " + insertRowId);

            		// Show Toast
					Toast toast = Toast.makeText(getApplicationContext(), R.string.searching_spam_db, Toast.LENGTH_SHORT);            					
					toast.setGravity(Gravity.BOTTOM, 0, 90);
					toast.show();
					
					// Start thread for SearchAndNotiActivity
            		Thread thread = new Thread(new Runnable() {
            			public void run() {
            				try {
                        		// Waiting Dial activity starting
                        		Thread.sleep(2000);
            					         		
                        		// Start SearchAndNotiActivity
            					Intent intent = new Intent(SpamFilterService.this, SearchAndNotiActivity.class);
                    			intent.putExtra("cid", inComingNumber);
                    			intent.putExtra("rowId", insertRowId);
                    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    			startActivity(intent);
                    			
                    			Util.log(logApplicationTag, logClassTag,  "in Thread - started Activity(SearchAndNotiActivity)");            					
            				}
            				catch (Throwable t) {            					
            				}
            			}
            		});
            		thread.start();
            		
            		break;
            	default:
            		Util.log(logApplicationTag, logClassTag,  "UNKNOWN STATE");
                }  
        	}
        };
        telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        // 안택수 추가 부분 알람 등록
        
        Intent intent = new Intent(SpamFilterService.this, DailyNotifyReceiver.class);
   		PendingIntent pIntent = PendingIntent.getBroadcast(SpamFilterService.this, 0, intent, 0);
   		
        AlarmManager alm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        // 매일 14시 00분마다 반복
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        if(date.getHours() > 14)
        	date.setDate(date.getDate()+1);
        date.setHours(14);
   		date.setMinutes(0);
   		date.setSeconds(0);
        alm.setRepeating(AlarmManager.RTC, date.getTime(), 24*60*60*1000, pIntent);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Unset PhoneStateListener
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		
		//Unset CallLogDb
		if (callLogDb != null) {
			callLogDb.close();
		}
	}
}

