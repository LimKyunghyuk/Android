package com.telcoware.taxicalluser;

import android.content.*;
import android.graphics.*;
import android.graphics.Shader.*;
import android.util.*;
import android.view.*;

public class CVHeader extends View
{	
	public CVHeader(Context context)
	{
		super(context);
	}
	
	public CVHeader(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		LinearGradient shader = new LinearGradient(0, 0, 0, getHeight(), Color.rgb(110, 110, 110), Color.rgb(60, 60, 60), TileMode.CLAMP);
		paint.setShader(shader);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
	}
}
