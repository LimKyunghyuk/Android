package kr.priv.android_sdcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActMain extends Activity {
	/** Called when the activity is first created. */

	EditText txtData;
	Button btnWriteSDFile;
	Button btnReadSDFile;
	Button btnClearScreen;
	Button btnClose;
	WebView v;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txtData = (EditText) findViewById(R.id.txtData);
		txtData.setHint("Enter some line of data here2");
		btnWriteSDFile = (Button) findViewById(R.id.btnWriteSDFile);
		btnReadSDFile = (Button) findViewById(R.id.btnReadSDFile);
		btnClearScreen = (Button) findViewById(R.id.btnClearScreen);
		btnClose = (Button) findViewById(R.id.btnClose);
		v = (WebView)findViewById(R.id.webview);
		
		btnWriteSDFile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "클릭 1번", 0).show();
				try {
					File myFile = new File("/sdcard/myadfile.txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					
					myOutWriter.append(txtData.getText());
					myOutWriter.close();
					
					
					
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), 0).show();
				}

			}
		});
		
		btnReadSDFile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "클릭2번", 0).show();
				
				try {
					File myFile = new File("/sdcard/ppt.ppt");
					FileInputStream fin = new FileInputStream(myFile);
					BufferedReader myReader = new BufferedReader(new InputStreamReader(fin));
					String aDataRow = "";
					String aBuffer = "";
					
					
					while((aDataRow = myReader.readLine()) != null){
						aBuffer += aDataRow + "\n";
					}
					
					txtData.setText(aBuffer);

				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), "클릭 2 : "+ex.getMessage(), 0).show();
				}

			}
		});
		
		btnClearScreen.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				txtData.setText("");

			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				finish();

			}
		});

	}
}