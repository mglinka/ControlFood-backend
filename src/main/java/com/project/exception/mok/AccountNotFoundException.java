package com.project.exception.mok;

import com.project.exception.abstract_exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
