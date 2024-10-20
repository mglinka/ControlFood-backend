package com.project.exception.mok;

import com.project.exception.abstract_exception.NotFoundException;

public class TokenNotFoundException extends NotFoundException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
