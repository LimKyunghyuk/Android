package com.khlim.push;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Android implements Http{

    final static long TIME_WAIT = 1000L;
    final static String properties = "config.properties";

    private URL url;
    private HttpURLConnection http;

    String fcmUrl;
    String accessToken;

    public Android(){

        Configurations configs = new Configurations();
        try {

            Configuration config = configs.properties(new File(properties));
            fcmUrl = config.getString("android.fcmUrl");
            accessToken = config.getString("android.accessToken");

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


        try {

            url = new URL(fcmUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("fcmUrl : " + fcmUrl);
        System.out.println("accessToken : " + accessToken);

    }

    @Override
    public void httpHeader() {

        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        http.setConnectTimeout(1000);   // 서버 연결 Timeout
        http.setReadTimeout(1000);      // InputStream 읽는 Timeout

        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setUseCaches(false);
        http.setDefaultUseCaches(false);

        http.setRequestProperty("Authorization", accessToken);
        http.setRequestProperty("Content-Type", "application/json; UTF-8");
    }

    @Override
    public void httpBody(String contents){

        OutputStream os = null;

        try {
            os = http.getOutputStream();
            os.write(contents.getBytes());
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String setReturn(){

        StringBuilder sb = new StringBuilder();
        try {
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(http.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println("1>>" + sb.toString());
            } else {
                System.out.println("2>>" + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String sendHttp(String target, String title, String body) {

        String retMsg;

        httpHeader();

        JSONObject notification = new JSONObject();
        notification.put("title",title);
        notification.put("message","알림 내용");
        notification.put("body",body);
        notification.put("sound","default");

        JSONObject json = new JSONObject();
        json.put("to", target);
        json.put("notification", notification);


        httpBody(json.toJSONString());

        retMsg = setReturn();

        try {
            Thread.sleep(TIME_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return retMsg;
    }


}
