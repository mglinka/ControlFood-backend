package com.project.exception.mok;

import com.project.exception.abstract_exception.BadRequestException;

public class RoleNotAssignedToAccount extends BadRequestException {

    public RoleNotAssignedToAccount(String message) {
        super(message);
    }
}
