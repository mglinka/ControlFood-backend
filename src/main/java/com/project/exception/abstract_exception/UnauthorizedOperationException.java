package com.project.exception.abstract_exception;

public class UnauthorizedOperationException extends AppException{
    protected UnauthorizedOperationException(String message) {
        super(message);
    }
}
