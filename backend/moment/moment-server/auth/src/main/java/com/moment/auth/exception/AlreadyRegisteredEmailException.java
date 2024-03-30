package com.moment.auth.exception;

public class AlreadyRegisteredEmailException extends RuntimeException{
    public AlreadyRegisteredEmailException(String message) {
        super(message);
    }
}
