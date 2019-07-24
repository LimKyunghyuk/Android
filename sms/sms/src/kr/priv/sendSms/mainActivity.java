package kr.priv.sendSms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.telephony.gsm.SmsManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class mainActivity extends Activity implements OnClickListener {

	Button btn1, btn2;
	EditText edt;
	String phoneNumber;
	String sosNumber;
	String preMsg;
	String inMsg;
	String postMsg;
	String addrMsg;
	String msg;
	
	

    protected void showDialog() {

            // This example shows how to add a custom layout to an AlertDialog
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
            edt = (EditText) textEntryView.findViewById(R.id.phoneNum_edit);
            new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle("SOS-PhoneNumber")
                .setView(textEntryView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
    
                    	phoneNumber = edt.getText().toString();
                    	Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();
                        /* User clicked OK so do some stuff */
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .show();
        }

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btn1 = (Button) findViewById(R.id.btn_1);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.btn_2);
		btn2.setOnClickListener(this);

	    
	}
	
	public void onClick(View v) {

		// 아래 2개의 변수만 연동하면 됨
		sosNumber = "01099588759";	// 도움을 요청할 번호
		addrMsg = "구로구 구로동 11-2";

		
		
		
		preMsg = "긴급상황/";	
		inMsg = "/기사번호";
		postMsg = "/도와주세요";
		
		msg = preMsg + addrMsg + inMsg + sosNumber + postMsg;
		//("긴급상황/구로구 구로동 11-2/기사번호 010-222-3333/도와주세요")
		
		switch (v.getId()) {

		
		case R.id.btn_1:
			
			Toast.makeText(getApplicationContext(), "문자전송" , Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
		
			SmsManager sm = SmsManager.getDefault();
			sm.sendTextMessage(phoneNumber, null, msg, null, null);
			
			break;
		case R.id.btn_2:
			
			showDialog();
		    break;
		}

	}
}
