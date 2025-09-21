package com.devasee.users.exceptions;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String message){
        super(message);
    }
}
