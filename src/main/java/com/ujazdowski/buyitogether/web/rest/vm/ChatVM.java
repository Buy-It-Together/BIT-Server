package com.ujazdowski.buyitogether.web.rest.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatVM implements Serializable {

    private Long id;

    private Set<UserOfferChatVM> userOfferChatVMs = new HashSet<>();

    private List<MessageVM> messages = new ArrayList<>();

    private Double suggested_latitude;

    private Double suggested_longitude;

    public ChatVM() {
    }

    public ChatVM(Long id, Set<UserOfferChatVM> userOfferChatVMs, List<MessageVM> messages, Double suggested_latitude, Double suggested_longitude) {
        this.id = id;
        this.userOfferChatVMs = userOfferChatVMs;
        this.messages = messages;
        this.suggested_latitude = suggested_latitude;
        this.suggested_longitude = suggested_longitude;
    }

    public ChatVM(Long id, Set<UserOfferChatVM> userOfferChatVMs) {
        this.id = id;
        this.userOfferChatVMs = userOfferChatVMs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserOfferChatVM> getUserOfferChatVMs() {
        return userOfferChatVMs;
    }

    public void setUserOfferChatVMs(Set<UserOfferChatVM> userOfferChatVMs) {
        this.userOfferChatVMs = userOfferChatVMs;
    }

    public Double getSuggested_latitude() {
        return suggested_latitude;
    }

    public void setSuggested_latitude(Double suggested_latitude) {
        this.suggested_latitude = suggested_latitude;
    }

    public Double getSuggested_longitude() {
        return suggested_longitude;
    }

    public void setSuggested_longitude(Double suggested_longitude) {
        this.suggested_longitude = suggested_longitude;
    }

    public List<MessageVM> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageVM> messages) {
        this.messages = messages;
    }
}
