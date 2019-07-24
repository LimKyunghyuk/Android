package kr.priv.p;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
public class Activity2 extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2);
         
        Button terminate = (Button)findViewById(R.id.terminateActivity);
        terminate.setOnClickListener(new Button.OnClickListener(){
 
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish(); // 액티비티를 종료합니다.
            }
             
        });
     
        // TODO Auto-generated method stub
    }
 
} 
