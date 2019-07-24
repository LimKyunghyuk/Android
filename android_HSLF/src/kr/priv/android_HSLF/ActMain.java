package kr.priv.android_HSLF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.SlideShow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;



public class ActMain extends Activity {
    /** Called when the activity is first created. */
	SlideShow ppt;
	int current = 0; 
	int all=0; 
	String filename; 
	String text;
	Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        btn = (Button) findViewById(R.id.btn);
		
        btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "클릭 1번", 0).show();
				
				
				
				 try {
						ppt = new SlideShow(new HSLFSlideShow("/sdcard/ppt.ppt"));
//						Slide[] slides = ppt.getSlides(); 
//						
//						
//						int c = slides.length;
//						
//						Toast.makeText(getApplicationContext(), "cnt : " + c, 0).show();
////						
						
//						Shape[] shapes = slides[0].getShapes();
//						
//						TextBox a = (TextBox)shapes[0];
//						
//						text = a.getText();
////						InputStream is = conn.getInputStream();
////						BufferedInputStream bis = new BufferedInputStream(is);
////						bm = BitmapFactory.decodeStream(bis);
//						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			}
		});
		
       
        
    }
}