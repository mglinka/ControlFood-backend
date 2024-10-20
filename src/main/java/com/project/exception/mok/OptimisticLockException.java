package com.project.exception.mok;

import com.project.exception.abstract_exception.PreconditionFailedException;

public class OptimisticLockException extends PreconditionFailedException {

    public OptimisticLockException(String message) {
        super(message);
    }
}
