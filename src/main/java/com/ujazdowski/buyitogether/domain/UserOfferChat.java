package com.ujazdowski.buyitogether.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserOfferChat.
 */
@Entity
@Table(name = "user_offer_chat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserOfferChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("userOfferChats")
    private UserOffer userOffer;

    public UserOfferChat id(Long id) {
        this.setId(id);
        return this;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public UserOfferChat accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Chat getChat() {
        return chat;
    }

    public UserOfferChat chat(Chat chat) {
        this.chat = chat;
        return this;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UserOffer getUserOffer() {
        return userOffer;
    }

    public UserOfferChat userOffer(UserOffer userOffer) {
        this.userOffer = userOffer;
        return this;
    }

    public void setUserOffer(UserOffer userOffer) {
        this.userOffer = userOffer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserOfferChat userOfferChat = (UserOfferChat) o;
        if (userOfferChat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userOfferChat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserOfferChat{" +
            "id=" + getId() +
            ", accepted='" + isAccepted() + "'" +
            "}";
    }
}
