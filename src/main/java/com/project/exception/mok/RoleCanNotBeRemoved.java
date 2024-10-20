package com.project.exception.mok;

import com.project.exception.abstract_exception.BadRequestException;

public class RoleCanNotBeRemoved extends BadRequestException {

    public RoleCanNotBeRemoved(String message) {
        super(message);
    }
}
