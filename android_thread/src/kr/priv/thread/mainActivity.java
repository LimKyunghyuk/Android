package kr.priv.thread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.os.Message;

public class mainActivity extends Activity {
    /** Called when the activity is first created. */
	TextView txt1;
	int oper=1;
	 ProgressDialog dialog;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txt1 = (TextView)findViewById(R.id.txt1);
        
        
        
        Background top = new Background(mHandler);
        Thread thd = new Thread(top);
        thd.setDaemon(true);
        thd.start();
        
        dialog = new ProgressDialog(mainActivity.this);
    	dialog.setMessage("전송중입니다.");
    	dialog.show();
        DialogProgress();
        
    }
    private void DialogProgress(){
    	
	}
    
    Handler mHandler = new Handler(){
    	public void handleMessage(Message msg){
    		if(msg.what == 0){
    			txt1.setText("BackText :"+ msg.arg1);
    		}
    		if(msg.what == 1){
    			dialog.cancel();
    		}
    		
    	}
    };
}