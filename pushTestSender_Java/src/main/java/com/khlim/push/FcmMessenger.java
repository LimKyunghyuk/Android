package com.khlim.push;

import java.io.*;
import java.util.List;

public class FcmMessenger {

    private Http http;

    public void setType(Http http){
        this.http = http;
    }

    public String push(String target, String title, String body) {
        return this.http.sendHttp(target, title, body);
    }

    public String push(Client client) {
        return this.http.sendHttp(client.getTarget(), client.getTitle(), client.getBody());
    }

    public String push(List<Client> clientList) {

        for(Client client : clientList){
            push(client);
            System.out.println(client.getTitle());
        }

        return new String("SUCCESS");
    }

}
