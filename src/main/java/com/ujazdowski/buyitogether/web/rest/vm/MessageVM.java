package com.ujazdowski.buyitogether.web.rest.vm;

import java.io.Serializable;
import java.time.Instant;

public class MessageVM implements Serializable {
    private String email;
    private String message;
    private Instant date;

    public MessageVM(String email, String message, Instant date) {
        this.email = email;
        this.message = message;
        this.date = date;
    }

    public MessageVM() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
