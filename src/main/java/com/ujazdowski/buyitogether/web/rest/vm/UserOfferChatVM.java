package com.ujazdowski.buyitogether.web.rest.vm;

import java.io.Serializable;

public class UserOfferChatVM implements Serializable {

    private Long id;

    private Boolean accepted;

    private UserOfferVM userOfferVM;

    private Boolean my;

    public UserOfferChatVM(Long id, Boolean accepted, UserOfferVM userOfferVM, Boolean my) {
        this.id = id;
        this.accepted = accepted;
        this.userOfferVM = userOfferVM;
        this.my = my;
    }

    public UserOfferChatVM() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public UserOfferVM getUserOfferVM() {
        return userOfferVM;
    }

    public void setUserOfferVM(UserOfferVM userOfferVM) {
        this.userOfferVM = userOfferVM;
    }

    public Boolean getMy() {
        return my;
    }

    public void setMy(Boolean my) {
        this.my = my;
    }
}
