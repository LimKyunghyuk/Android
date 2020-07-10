package com.khlim.push;

import java.io.IOException;
import java.net.ProtocolException;

public interface Http {
    public void httpHeader();
    public void httpBody(String contents);

    public String sendHttp(String target, String title, String body);
}
