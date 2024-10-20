package com.project.exception.mok;

import com.project.exception.abstract_exception.BadRequestException;

public class PasswordResetTokenUsedException extends BadRequestException {

    public PasswordResetTokenUsedException(String message) {
        super(message);
    }
}
