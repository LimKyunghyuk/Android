package kr.priv.android_presentation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActMain extends Activity {
	/** Called when the activity is first created. */

	private final static String NOTES = "note.txt";
	
	Intent intent;
	
	BufferedReader br;
	InputStream in;

	EditText edt;
	
	@Override
	public void onPause(){
		try{
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(NOTES,0));
			out.write(edt.getText().toString());
			out.close();
		}catch(Throwable t){
			
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		Button btn = (Button) findViewById(R.id.Button01);
		edt = (EditText)findViewById(R.id.EditText01);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "클릭", 0).show();
				try{
					
					in = openFileInput(NOTES);
					if(in!=null){
						
						String str;
						InputStreamReader tmp = new InputStreamReader(in);
						BufferedReader reader = new BufferedReader(tmp);
						StringBuffer buf = new StringBuffer();
						
						while((str = reader.readLine()) != null){
							
							buf.append(str + "\n");
						}
						
						in.close();
						
						edt.setText(buf.toString());
						
					}
					
					
					
					
					
				}catch(FileNotFoundException ex){
					
					
				}catch(Throwable t){
					
					
				}
				
			}
		});
		// try {
		//
		// //br = new BufferedReader(new FileReader("/sdcard/ppt.txt"));
		// br = new BufferedReader(new InputStreamReader(
		// openFileInput("/sdcard/ppt.txt")));
		// while((str+=br.readLine())!=null);
		//		
		// br.close();
		//
		// } catch (FileNotFoundException e1) {
		// e1.printStackTrace();
		// }
		//
		// catch (IOException e) {
		// e.printStackTrace();
		// }

		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_VIEW);
		// Uri uri = Uri.parse("/sdcard/ppt.ppt");
		// intent.setDataAndType(uri,"application/vnd.ms-powerpoint");
		// intent.putExtra(intent.EXTRA_STREAM, uri);
		// startActivity(intent);

		// String path = android.os.Environment.getExternalStorageDirectory()
		// .getAbsolutePath();
		//
		// String url = "http://mycom.priv.kr/ppt.ppt";
		//
		// String aboutHtml = HttpConnect.postData(url);
		//
		// WebView v = (WebView)findViewById(R.id.webview);
		//		
		//		
		// v.loadData(aboutHtml, "text/html", "utf-8");

		// android.content.res.Resources resources = this.getResources();
		// InputStream aboutHtmlInputStream = new
		// BufferedInputStream(resources.openRawResource(R.raw.about));

		// String aboutHtml = null;
		// try {
		// aboutHtml = StreamUtils.readStringFully(aboutHtmlInputStream);
		// aboutHtmlInputStream.close();
		// aboutHtmlInputStream = null;
		// resources = null;
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }

		// Log.d("  ",path);
		// intent = new Intent();
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setData(Uri.parse("http://mycom.priv.kr/t.pdf"));
		// //intent.setDataAndType(/, "");
		// //
		// intent.setAction(Intent.ACTION_SEND);
		// intent.setType("application/vnd.ms-powerpoint");
		// intent.putExtra(intent.EXTRA_STREAM,Uri.parse("sdcard/t.pdf"));
		// // startActivity(intent);
		//	    
		// startActivity(intent);
		//        
		// WebView wv = (WebView)findViewById(R.id.webview);
		// wv.loadUrl("http://mycom.priv.kr/t.pdf");
		// wv.getSettings().setJavaScriptEnabled(true);

	}
}