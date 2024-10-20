package com.project.exception.auth;


import com.project.exception.abstract_exception.NotFoundException;

public class AccountConfirmationTokenNotFoundException extends NotFoundException {
    public AccountConfirmationTokenNotFoundException(String message) {
        super(message);
    }
}
