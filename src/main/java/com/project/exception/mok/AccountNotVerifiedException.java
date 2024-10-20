package com.project.exception.mok;

import com.project.exception.abstract_exception.UnauthorizedOperationException;

public class AccountNotVerifiedException extends UnauthorizedOperationException {

    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
