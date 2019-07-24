package kr.priv.t;


import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.app.Activity;

public class first extends Activity {
	/** Called when the activity is first created. */
	Button btnChk;
	Button btnSnd;
	private Menu mMenu;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);

		
		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		TagNode node = null;
		TagNode t;
		
		ArrayList<String> prmtr = new ArrayList<String>();
		prmtr.add("http://speller.cs.pusan.ac.kr/PnuSpellerISAPI_201009/lib/PnuSpellerISAPI_201009.dll?Check");
		prmtr.add("appll");
		
		try{
			
			
			node = cleaner.clean(HttpConnect.postData(prmtr));
			
			Object[] objArray = node.evaluateXPath("//table//tr//td");
			for(Object obj:objArray){
				t = (TagNode)obj;
				Toast.makeText(getApplicationContext(), t.getText().toString(),
						Toast.LENGTH_SHORT).show();
			}
			
		} catch(IOException e){
			e.printStackTrace();
			
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		btnChk = (Button) findViewById(R.id.btn_check);
		btnChk.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "검사 중..",
						Toast.LENGTH_SHORT).show();
				onCreateDialog();
			}

		});

		btnSnd = (Button) findViewById(R.id.btn_send);
		btnSnd.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				Toast.makeText(getApplicationContext(), "전송 중..",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_CALL_BUTTON, null);
				startActivity(intent);

			}

		});

	}
}