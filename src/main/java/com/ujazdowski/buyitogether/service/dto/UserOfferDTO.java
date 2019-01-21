package com.ujazdowski.buyitogether.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserOffer entity.
 */
public class UserOfferDTO implements Serializable {

    private Long id;

    @NotNull
    private String link;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    private Double distance;

    @NotNull
    private Integer countOfItems;

    @NotNull
    private Integer countOfItemsToGetBonus;

    @NotNull
    private Instant expirationDate;

    private Long userId;

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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getCountOfItems() {
        return countOfItems;
    }

    public void setCountOfItems(Integer countOfItems) {
        this.countOfItems = countOfItems;
    }

    public Integer getCountOfItemsToGetBonus() {
        return countOfItemsToGetBonus;
    }

    public void setCountOfItemsToGetBonus(Integer countOfItemsToGetBonus) {
        this.countOfItemsToGetBonus = countOfItemsToGetBonus;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserOfferDTO userOfferDTO = (UserOfferDTO) o;
        if (userOfferDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userOfferDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserOfferDTO{" +
            "id=" + getId() +
            ", link='" + getLink() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", distance=" + getDistance() +
            ", countOfItems=" + getCountOfItems() +
            ", countOfItemsToGetBonus=" + getCountOfItemsToGetBonus() +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
