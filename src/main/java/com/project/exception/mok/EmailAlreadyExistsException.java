package com.project.exception.mok;

import com.project.exception.abstract_exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
