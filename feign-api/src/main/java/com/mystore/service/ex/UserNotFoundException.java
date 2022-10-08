package com.mystore.service.ex;

public class UserNotFoundException extends ServiceException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
