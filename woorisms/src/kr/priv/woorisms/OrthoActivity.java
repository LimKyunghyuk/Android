package kr.priv.woorisms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrthoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orthography);
		
		Button btn = (Button)findViewById(R.id.Button01);
		btn.setOnClickListener(new Button.OnClickListener(){
			
			public void onClick(View v){
				
				finish();
			
			}
		});
		
		
		
	}

}
