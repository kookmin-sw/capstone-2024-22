package com.moment.core.exception;

public class AlreadyBookedDateException extends RuntimeException
{
    public AlreadyBookedDateException(String message) {
        super(message);
    }
}
