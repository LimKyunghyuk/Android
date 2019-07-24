package com.telcoware.whoareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "BootCompletedReceiver";

	private static final String ACTION = "android.intent.action.BOOT_COMPLETED"; 

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub	
        if (intent.getAction().equals(ACTION)) {
        	Util.log(logApplicationTag, logClassTag, "Receive BOOT COMPLETED");
            context.startService(new Intent(context, SpamFilterService.class));
        } 
	}
}
