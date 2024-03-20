package com.moment.auth.exception;

public class AlreadyBookedDateException extends RuntimeException
{
    public AlreadyBookedDateException(String message) {
        super(message);
    }
}
