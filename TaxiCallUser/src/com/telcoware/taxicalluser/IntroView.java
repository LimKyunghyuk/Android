package com.telcoware.taxicalluser;

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
		// super.onDraw(canvas);
		canvas.drawColor(Color.BLUE);

		Bitmap jayPic;
		jayPic = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_intro);

		int mH = getMeasuredHeight();
		int mW = getMeasuredWidth();

		// Draw the big middle jay
		Bitmap jayPicMedium = Bitmap.createScaledBitmap(jayPic, mW, mH, false);
		canvas.drawBitmap(jayPicMedium, 0, 0, null);
	}

	public IntroView(Context context) {
		super(context);
	}
}
