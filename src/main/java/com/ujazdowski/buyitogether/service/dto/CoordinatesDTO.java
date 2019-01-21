package com.ujazdowski.buyitogether.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class CoordinatesDTO implements Serializable {

    private Double latitude;

    private Double longitude;

    public CoordinatesDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoordinatesDTO)) return false;
        CoordinatesDTO that = (CoordinatesDTO) o;
        return Objects.equals(getLatitude(), that.getLatitude()) &&
            Objects.equals(getLongitude(), that.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }
}
