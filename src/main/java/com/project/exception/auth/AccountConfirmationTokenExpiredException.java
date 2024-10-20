package com.project.exception.auth;

import com.project.exception.abstract_exception.ConflictException;

public class AccountConfirmationTokenExpiredException extends ConflictException {

    public AccountConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
