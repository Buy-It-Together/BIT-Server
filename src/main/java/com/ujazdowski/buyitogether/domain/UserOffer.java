package com.ujazdowski.buyitogether.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A UserOffer.
 */
@Entity
@Table(name = "user_offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserOffer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "distance", nullable = false)
    private Double distance;

    @NotNull
    @Column(name = "count_of_items", nullable = false)
    private Integer countOfItems;

    @NotNull
    @Column(name = "count_of_items_to_get_bonus", nullable = false)
    private Integer countOfItemsToGetBonus;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @OneToMany(mappedBy = "userOffer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserOfferChat> userOfferChats = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public UserOffer link(String link) {
        this.link = link;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public UserOffer longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public UserOffer latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public UserOffer distance(Double distance) {
        this.distance = distance;
        return this;
    }

    public Integer getCountOfItems() {
        return countOfItems;
    }

    public void setCountOfItems(Integer countOfItems) {
        this.countOfItems = countOfItems;
    }

    public UserOffer countOfItems(Integer countOfItems) {
        this.countOfItems = countOfItems;
        return this;
    }

    public Integer getCountOfItemsToGetBonus() {
        return countOfItemsToGetBonus;
    }

    public void setCountOfItemsToGetBonus(Integer countOfItemsToGetBonus) {
        this.countOfItemsToGetBonus = countOfItemsToGetBonus;
    }

    public UserOffer countOfItemsToGetBonus(Integer countOfItemsToGetBonus) {
        this.countOfItemsToGetBonus = countOfItemsToGetBonus;
        return this;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserOffer expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public Set<UserOfferChat> getUserOfferChats() {
        return userOfferChats;
    }

    public void setUserOfferChats(Set<UserOfferChat> userOfferChats) {
        this.userOfferChats = userOfferChats;
    }

    public UserOffer userOfferChats(Set<UserOfferChat> userOfferChats) {
        this.userOfferChats = userOfferChats;
        return this;
    }

    public UserOffer addUserOfferChat(UserOfferChat userOfferChat) {
        this.userOfferChats.add(userOfferChat);
        userOfferChat.setUserOffer(this);
        return this;
    }

    public UserOffer removeUserOfferChat(UserOfferChat userOfferChat) {
        this.userOfferChats.remove(userOfferChat);
        userOfferChat.setUserOffer(null);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserOffer user(User user) {
        this.user = user;
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
        UserOffer userOffer = (UserOffer) o;
        if (userOffer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userOffer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserOffer{" +
            "id=" + getId() +
            ", link='" + getLink() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", distance=" + getDistance() +
            ", countOfItems=" + getCountOfItems() +
            ", countOfItemsToGetBonus=" + getCountOfItemsToGetBonus() +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
