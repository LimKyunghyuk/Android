package main;

import java.io.*;

public class FcmMessenger {

    private Http http;

    public void setType(Http http){
        this.http = http;
    }


    public String push(String target, String title, String body) {

        return this.http.send(target, title, body);
    }

}
