package com.security.service.Exceptions;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String message){super(message);}
}
