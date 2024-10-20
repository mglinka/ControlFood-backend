package com.project.exception.mok;

import com.project.exception.abstract_exception.ConflictException;

public class RoleAlreadyAssignedException extends ConflictException {

    public RoleAlreadyAssignedException(String message) {
        super(message);
    }
}
