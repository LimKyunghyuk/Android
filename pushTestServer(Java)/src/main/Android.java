package main;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Android implements Http{

    final static String url2 = "https://fcm.googleapis.com/fcm/send";
    final static String accessToken = "key=AAAAk2yEJ7w:APA91bFOK2QYz0PoRRgebr8YzIy9rThlfQ03PWCROsOG-48EsOSETMvOeXpmGgqWvJu70y7rtlo7WDGvdgGfLdpdoHwnMQc0xyRFr5gATgMYHy_IjTh-xArC7394zRihV1e7DwrroVuG";

    HttpURLConnection http;

    Android(){

        try {
            URL url = new URL(url2);
            http = (HttpURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setHttpHeader() {

            http.setConnectTimeout(5000);   //서버 연결되 Timeout
            http.setReadTimeout(5000);      // InputStream 읽어 오는 Timeout

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
    public void setHttpBody(String contents){

        OutputStream os = null;

        try {
            os = http.getOutputStream();
            os.write(contents.getBytes()); //json 형식의 message 전달
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
//                System.out.println("" + sb.toString());
            } else {
                System.out.println(http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String send(String target, String title, String body) {
        setHttpHeader();

        JSONObject json = new JSONObject();

        JSONObject notification = new JSONObject();

        notification.put("title",title);
        notification.put("message","알림 내용");
        notification.put("body",body);
        notification.put("sound","default");

        json.put("to", target);
        json.put("notification", notification);


        setHttpBody(json.toJSONString());

        return setReturn();
    }


}
