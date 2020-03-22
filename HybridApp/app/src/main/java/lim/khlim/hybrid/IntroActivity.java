package lim.khlim.hybrid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    public final static long INTRO_PASS_TIME = 300; // ms

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() { // INTRO_PASS_TIME 뒤에 다음화면(MainActivity)으로 넘어가기 Handler 사용
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);                                      // 다음화면으로 넘어가기
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);   // 애니메이션 효과 적용
            finish();                                                   // Activity 화면 제거
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 다시 화면에 들어어왔을 때 예약 걸어주기
        handler.postDelayed(r, INTRO_PASS_TIME); // INTRO_PASS_TIME 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(r); // 화면을 벗어나면 handler 취소
    }

}
