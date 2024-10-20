package com.project.exception.mok;

import com.project.exception.abstract_exception.UnauthorizedOperationException;

public class AccountLockedException extends UnauthorizedOperationException {

    public AccountLockedException(String message) {
        super(message);
    }
}
