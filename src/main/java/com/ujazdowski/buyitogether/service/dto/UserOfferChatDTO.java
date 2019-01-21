package com.ujazdowski.buyitogether.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserOfferChat entity.
 */
public class UserOfferChatDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean accepted;

    private Long chatId;

    private Long userOfferId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getUserOfferId() {
        return userOfferId;
    }

    public void setUserOfferId(Long userOfferId) {
        this.userOfferId = userOfferId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserOfferChatDTO userOfferChatDTO = (UserOfferChatDTO) o;
        if (userOfferChatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userOfferChatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserOfferChatDTO{" +
            "id=" + getId() +
            ", accepted='" + isAccepted() + "'" +
            ", chat=" + getChatId() +
            ", userOffer=" + getUserOfferId() +
            "}";
    }
}
