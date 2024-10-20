package com.project.exception.mok;

import com.project.exception.abstract_exception.NotFoundException;

public class AccountUnlockTokenNotFoundException extends NotFoundException {

    public AccountUnlockTokenNotFoundException(String message) {
        super(message);
    }
}
