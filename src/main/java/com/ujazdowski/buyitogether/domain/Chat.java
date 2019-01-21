package com.ujazdowski.buyitogether.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserOfferChat> users = new HashSet<>();
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserOfferChat> getUsers() {
        return users;
    }

    public void setUsers(Set<UserOfferChat> userOfferChats) {
        this.users = userOfferChats;
    }

    public Chat users(Set<UserOfferChat> userOfferChats) {
        this.users = userOfferChats;
        return this;
    }

    public Chat addUsers(UserOfferChat userOfferChat) {
        this.users.add(userOfferChat);
        userOfferChat.setChat(this);
        return this;
    }

    public Chat addUsers(List<UserOfferChat> userOfferChat) {
        for (UserOfferChat offerChat : userOfferChat) {
            offerChat.setChat(this);
        }
        this.users.addAll(userOfferChat);
        return this;
    }

    public Chat removeUsers(UserOfferChat userOfferChat) {
        this.users.remove(userOfferChat);
        userOfferChat.setChat(null);
        return this;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Chat messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Chat addMessages(Message message) {
        this.messages.add(message);
        message.setChat(this);
        return this;
    }

    public Chat removeMessages(Message message) {
        this.messages.remove(message);
        message.setChat(null);
        return this;
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
        Chat chat = (Chat) o;
        if (chat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            "}";
    }
}
