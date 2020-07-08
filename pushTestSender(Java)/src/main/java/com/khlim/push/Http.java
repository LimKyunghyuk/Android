package com.khlim.push;

import java.io.IOException;
import java.net.ProtocolException;

public interface Http {
    public void setHttpHeader();
    public void setHttpBody(String contents);

    public String send(String target, String title, String body);
}
