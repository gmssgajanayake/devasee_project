package com.devasee.users.exceptions;

public class InvalidAuthHeaderException extends RuntimeException {
    public InvalidAuthHeaderException(String message){
        super(message);
    }
}
