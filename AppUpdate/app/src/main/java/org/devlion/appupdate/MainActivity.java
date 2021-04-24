package org.devlion.appupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);


        File PATH_INNER_STORAGE = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Log.d("APP_UPDATE", "Start!" + PATH_INNER_STORAGE);


        Button btn_dnload = (Button) findViewById(R.id.btn_download);
        btn_dnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AppUpdateManager(PATH_INNER_STORAGE, new OnEvent() {
                    @Override
                    public void onPreExecute() {
                        Toast.makeText(MainActivity.this, "onPreExecute", Toast.LENGTH_SHORT).show();
                        progressBar.setMax(100) ;
                        progressBar.setProgress(0) ;
                    }

                    @Override
                    public void onProgressUpdate(int count) {
                        progressBar.setProgress(count) ;
                    }

                    @Override
                    public void onPostExecute() {
                        Toast.makeText(MainActivity.this, "onPostExecute", Toast.LENGTH_SHORT).show();
                    }
                }).execute("");

            }
        });





    }
}