package com.ujazdowski.buyitogether.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomUser entity.
 */
public class CustomUserDTO implements Serializable {

    private Long id;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        CustomUserDTO customUserDTO = (CustomUserDTO) o;
        if (customUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomUserDTO{" +
            "id=" + getId() +
            ", user=" + getUserId() +
            "}";
    }
}
