package com.telcoware.whoareyou;

import java.util.StringTokenizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class DailyNotifyReceiver extends BroadcastReceiver
{

	SharedPreferences configPrefs;
	SharedPreferences.Editor prefsEditor;
	
	@Override
	public void onReceive(Context _context, Intent _intent)
	{
		try {
			configPrefs = _context.getSharedPreferences(_context.getResources().getString(R.string.config_preferences), 0);
			prefsEditor = configPrefs.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String db_feed = _context.getString(R.string.db_result_feed);
		
		String strDBResult = Util.getWebDocument(db_feed, 4000);
		
		StringTokenizer stok = new StringTokenizer(strDBResult, "\n");
		String dbTotal = null;
		
		// REGI 부분만 받아와서 수정
		while(stok.hasMoreTokens())
		{
			dbTotal = stok.nextToken();
		}
		dbTotal = dbTotal.replace("REGI:", "");
		// 사용자 등록 Spam 개수 입출력
		//Integer userReg = new Integer(configPrefs.getInt("myRegiSpam", 0));
		
		// 알림 메시지 발송
		NotificationManager notiManager = (NotificationManager)_context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification noti = new Notification(R.drawable.mainicon, "WhoAreYou Spam DB가 업데이트 되었습니다.", System.currentTimeMillis());
		noti.defaults |= Notification.DEFAULT_SOUND;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Intent intent = new Intent(_context, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(_context, 0, intent, 0);
		noti.setLatestEventInfo(_context, _context.getResources().getString(R.string.app_name), "총 " + dbTotal + "개의 DB가 등록되어 있습니다.\n"/* + "현재까지  " + userReg + "개의 스팸 번호를 서버에 등록했습니다."*/, pIntent);
		notiManager.notify(23, noti);
		// 안택수 추가부분 끝
	}
}