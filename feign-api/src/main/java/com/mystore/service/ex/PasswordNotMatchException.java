package com.mystore.service.ex;

public class PasswordNotMatchException  extends RuntimeException{
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
