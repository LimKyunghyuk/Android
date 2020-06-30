package lim.khlim.hybrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.os.Environment;
import android.Manifest;
import android.content.pm.PackageManager;

import android.webkit.DownloadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WebView webView; // 웹뷰
    private WebSettings webViewSetting; // 웹뷰 셋팅
    private String webUrlLocal = "http://appstore.lottechilsung.co.kr/nologin.sm"; // url
    //private String webUrlLocal = "file:///android_asset/www/playground/index.html";
    long downloadID;

    String filePath;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }

        webView = (WebView) findViewById(R.id.webView); // 웹뷰 id
        webViewSetting = webView.getSettings();             // testWebview를 webViewSetting 에 선언 해준다.

        webViewSetting.setJavaScriptEnabled(true);    // 웹의 자바스크립트 허용
        webViewSetting.setLoadWithOverviewMode(true); // 웹 화면을 디바이스 화면에 맞게 셋팅

        webView.setWebViewClient(new WebViewClient(){}); // 내부 webview로 열기
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                Toast.makeText(getApplicationContext(), "alert", Toast.LENGTH_SHORT).show();
                return super.onJsAlert(view, url, message, result);
            }


        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {

                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), contentDisposition, Toast.LENGTH_SHORT).show();

                Log.d("HYBRID", "url : " + url);
                Log.d("HYBRID", "contentDisposition : " + contentDisposition);
                Log.d("HYBRID", "mimeType : " + mimeType);
                Log.d("HYBRID", "contentLength : " + contentLength);

                loading();

                Uri urlToDownload = Uri.parse(url);
                List<String> pathSegments = urlToDownload.getPathSegments();

                for(String s : pathSegments){
                    Log.d("HYBRID", "s : " + s);
                }

                DownloadManager.Request request = new DownloadManager.Request(urlToDownload);

                request.setTitle("앱 다운로드");
                request.setDescription("ooo 입니다.");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);

                String[] fileName = url.split("=");

                for(String f : fileName){
                    Log.d("HYBRID", "f : " + f);
                }

                Log.d("HYBRID", "fileName : " + fileName[fileName.length-1]);

                // Download 폴더에 저장
                // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName[fileName.length-1]);


                // storage/emulated/0/Android/data/lim.khlim.hybrid/files/Download 폴더에 저장
                File f = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                request.setDestinationInExternalPublicDir(f.getAbsolutePath(), fileName[fileName.length-1]);

                filePath = f.getAbsolutePath() + "/" + fileName[fileName.length-1];

                Log.d("HYBRID", "path : " + filePath);


                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                downloadID = dm.enqueue(request);


                loadingEnd();
            }});




        webView.loadUrl(webUrlLocal);  // url 주소

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("잠시만 기다려 주세요");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {

                // 다운로드한 파일 실행
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();

                File apkFile = new File(filePath);


                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider",apkFile);
                Intent executeApp = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);


/*
                Intent executeApp = new Intent(Intent.ACTION_VIEW);



                    File apkFile = new File(filePath);
                    Uri apkUri = Uri.fromFile(apkFile);


                    Log.d("HYBRID", ">>f path" + filePath);




                    executeApp.setDataAndType(apkUri,"application/vnd.android.package-archive");
                    executeApp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(executeApp);
*/

                /*
                DownloadManager dm = new DownloadManager(context);

                openDownloadedFile();
                Intent executeApp = new Intent();



                executeApp.setDataAndType(path,"application/vnd.android.package-archive");

                startActivity(executeApp);
*/


            }
        }
    };


}
