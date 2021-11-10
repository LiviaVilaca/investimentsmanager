package com.liviavilaca.investimentsmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(Class entity, Long id) {
        super(String.format("%s with id %d not found", entity.getSimpleName(), id));
    }

    public EntityNotFoundException(Class entity) {
        super(String.format("%s not found", entity.getSimpleName()));
    }
}
