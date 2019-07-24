package com.telcoware.whoareyou;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class IntroView extends View {
	private static final String logApplicationTag = "WhoAreYou";
	private static final String logClassTag = "IntroView";

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//super.onDraw(canvas);
		canvas.drawColor(Color.BLUE);
		
		Bitmap jayPic;
		if (this.getWidth() < this.getHeight())
			jayPic = BitmapFactory.decodeResource(getResources(), R.drawable.intro);
		else
			jayPic = BitmapFactory.decodeResource(getResources(), R.drawable.intro_hor);
        
        int mH = getMeasuredHeight();
        int mW = getMeasuredWidth();
        
        Util.log(logApplicationTag, logClassTag, "Height : " + mH);
        Util.log(logApplicationTag, logClassTag, "Width : " + mW);
        
        // Draw the big middle jay
        Bitmap jayPicMedium= Bitmap.createScaledBitmap(jayPic, mW, mH, false);           
        canvas.drawBitmap(jayPicMedium, 0, 0, null);
	}
	
	public IntroView(Context context) {
		super(context);
	}
}
