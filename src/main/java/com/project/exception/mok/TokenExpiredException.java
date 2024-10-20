package com.project.exception.mok;

import com.project.exception.abstract_exception.ConflictException;

public class TokenExpiredException extends ConflictException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
