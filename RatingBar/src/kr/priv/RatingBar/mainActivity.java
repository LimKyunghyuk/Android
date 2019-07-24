package kr.priv.RatingBar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class mainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageView img = (ImageView) findViewById(R.id.ImageView01);

		img.setImageBitmap(myRatingBar(3));

	}

	private Bitmap myRatingBar(int num) {

		Bitmap bm = null;
		
		switch (num) {

		case 0:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star0);
			break;
		case 1:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star1);
			break;
		case 2:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star2);
			break;
		case 3:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star3);
			break;
		case 4:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star4);
			break;
		case 5:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star5);
			break;
		case 6:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star6);
			break;
		case 7:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star7);
			break;
		case 8:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star8);
			break;
		case 9:
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.star9);
			break;
		}
		
		
		return bm;
	}

}