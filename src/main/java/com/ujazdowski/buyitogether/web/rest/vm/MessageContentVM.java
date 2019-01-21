package com.ujazdowski.buyitogether.web.rest.vm;

import java.io.Serializable;

public class MessageContentVM implements Serializable {
    private String message;

    public MessageContentVM(String message) {
        this.message = message;
    }

    public MessageContentVM() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
