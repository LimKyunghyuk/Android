package org.devlion.appupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);

        Button btn_dnload = (Button) findViewById(R.id.btn_download);
        btn_dnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AppUpdateManager(MainActivity.this, new OnEvent() {
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
                    public void onPostExecute(File apk) {
                        Toast.makeText(MainActivity.this, "onPostExecute", Toast.LENGTH_SHORT).show();

                        try{
                            showApkInstaller(MainActivity.this, apk);
                        }catch(Exception e){
                            Log.d("APP_UPDATE", "APP_UPDATE>>" + e.toString());
                        }

                        Log.d("APP_UPDATE", "end?");


                    }
                }).execute("");

            }
        });





    }

    public void showApkInstaller(Context context, File f) throws FileNotFoundException {

        Log.d("APP_UPDATE", "apk2 : " + f);

//        File f = new File(apkPath);
        if(!f.exists()){
            throw new FileNotFoundException("Can not find apk file.");
        }


        Intent i = new Intent(Intent.ACTION_VIEW);

        String packageName = getApplicationContext().getPackageName();
        Uri contentUri = FileProvider.getUriForFile(context, packageName + ".provider", f);

        Log.d("APP_UPDATE", "apk3 : " + contentUri);


        i.setDataAndType(contentUri,
                "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 추가
        context.startActivity(i);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Log.d("APP_UPDATE", "install 1");
//
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 추가
//            i.setData(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", f)); // 추가
//            context.startActivity(i);
//
//
//            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider",f);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            context.startActivity(i);

            i.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        } else {
            Log.d("APP_UPDATE", "install 2");
            i.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        }

         */

    }

}