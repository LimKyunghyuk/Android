package kr.priv.android_LoadImg;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

public class mainActiviry extends Activity {
    /** Called when the activity is first created. */
	
	TextView txt1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
        ImageView g = (ImageView)findViewById(R.id.ImageView01);
        String myImagesURL = "http://mane.nasse.net/telco/img/dft.jpg";    
        g.setImageBitmap(loadImg(myImagesURL));
        */
        
        txt1 = (TextView)findViewById(R.id.TextView01);
        RatingBar vw_barTaxiGrade = (RatingBar)findViewById(R.id.ratingbar);
    }
   
    private Bitmap loadImg(String myImagesURL){

    	Bitmap bm;
    	
    	try {

			URL aURL = new URL(myImagesURL);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();

			return bm;
			
		} catch (IOException e) {

		//	g.setImageResource(R.drawable.icon);
			Log.d("DEBUGTAG", "Remtoe Image Exception");

		}
		
		return null;
		
	}
}