package com.ujazdowski.buyitogether.service.exceptions;

import javax.persistence.EntityNotFoundException;

public class EntityNotFountException extends EntityNotFoundException {
    public EntityNotFountException(Class entity, Long id) {
        super(new StringBuilder().append("Entity: ").append(entity.getSimpleName()).append(" id: ").append(id)
            .append("not found.").toString());
    }
}
