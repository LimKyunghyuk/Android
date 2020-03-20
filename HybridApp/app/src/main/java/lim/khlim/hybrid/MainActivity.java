package lim.khlim.hybrid;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.os.Bundle;
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

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WebView webView; // 웹뷰
    private WebSettings webViewSetting; // 웹뷰 셋팅
    private String webUrlLocal = "http://appstore.lottechilsung.co.kr/nologin.sm"; // url
    //private String webUrlLocal = "file:///android_asset/www/playground/index.html";

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

                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), contentDisposition, Toast.LENGTH_SHORT).show();

               // DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //

                Uri urlToDownload = Uri.parse(url);
                List<String> pathSegments = urlToDownload.getPathSegments();

                DownloadManager.Request request = new DownloadManager.Request(urlToDownload);

                request.setTitle("앱 다운로드");
                request.setDescription("ooo 입니다.");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pathSegments.get(pathSegments.size()-1));
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);

               // long latestId = downloadManager.enqueue(request);
                //downloadUrl.setText("");

                //







/*

                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading File...");
                request.setTitle(URLUtil.guessFileName(url, "123.apk", mimeType));
                //request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();

 */
            }});




        webView.loadUrl(webUrlLocal);  // url 주소

    }
}
