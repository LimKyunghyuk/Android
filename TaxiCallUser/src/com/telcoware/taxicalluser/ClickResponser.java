package com.telcoware.taxicalluser;

import android.content.*;
import android.os.*;

public class ClickResponser
{
	private Vibrator m_vibrator;
	
	public ClickResponser(Context context)
	{
		m_vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public void vibrate()
	{
		m_vibrator.vibrate(30);
	}
}
