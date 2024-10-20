package com.project.exception.mok;

import com.project.exception.abstract_exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
