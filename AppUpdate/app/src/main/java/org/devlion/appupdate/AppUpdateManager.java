package org.devlion.appupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppUpdateManager extends AsyncTask<String, Integer, Boolean> {

    public static final String fileUrl = "http://yorsild.dothome.co.kr/app.apk";

    private Context mContext = null ;

    OnEvent onEvent;
    File PATH_INNER_STORAGE;
    AppUpdateManager(File file, OnEvent onEvent){
        this.onEvent = onEvent;
        this.PATH_INNER_STORAGE = file;
    }

    // #1 in main thread
    @Override
    protected void onPreExecute() {     // 초기화
        Log.d("APP_UPDATE", "onPreExecute()");
        onEvent.onPreExecute();
    }

    // #2 in back ground thread
    @Override
    protected Boolean doInBackground(String... strings) {
        Log.d("APP_UPDATE", "doInBackground()");
        File dir = new File(savePath);
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileUrl = "http://yorsild.dothome.co.kr/app.apk";
//        String fileUrl = "http://yorsild.dothome.co.kr/img.png";

        localPath = savePath + "app.apk";


        Log.d("APP_UPDATE", "localPath: " + localPath);

        try {
            URL url = new URL(fileUrl);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int response = conn.getResponseCode();
            int fileLength = conn.getContentLength();

            Log.d("APP_UPDATE", "fileLength :" + fileLength);


            // download the file
            InputStream is = new BufferedInputStream(url.openStream(), 8192);

//            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName);
            File file = new File(PATH_INNER_STORAGE, "app.apk");

//            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName);

            OutputStream outStream = new FileOutputStream(file);


//            File file = new File(localPath);

//            InputStream is = conn.getInputStream();
//            OutputStream outStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len = 0;

            long total = 0;


            while ((len = is.read(buf)) > 0) {

                total += len;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));

                outStream.write(buf, 0, len);
            }

            outStream.close();
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    // #3 in main thread
    // doInBackground() 메서드에서 publishProgress() 메서드를 호출했을 때,
    // 메인 UI 스레드에서 실행할 onProgressUpdate() 메서드를 구현
    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("APP_UPDATE", "onProgressUpdate()");
        onEvent.onProgressUpdate(values[0]);
    }

    // #4 in main thread
    @Override
    protected void onPostExecute(Boolean result) {
        Log.d("APP_UPDATE", "onPostExecute()");
        onEvent.onPostExecute();
    }

    String savePath = Environment.getExternalStorageDirectory() + File.separator + "temp/";
    String localPath;


}

//https://calrip.tistory.com/entry/Android-11-%EB%B2%94%EC%9C%84-%EC%A7%80%EC%A0%95-%EC%A0%80%EC%9E%A5%EC%86%8C-%EC%97%85%EB%8D%B0%EC%9D%B4%ED%8A%B8-%EB%8C%80%EC%9D%91