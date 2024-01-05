package com.denisvasilenko.blogapp.exceptions.userException;

import java.util.Date;

public class UserAlreadyExist extends RuntimeException {

    private final String message;
    private final Date timestamp;

    public UserAlreadyExist(String username) {
        message = "User "+"'"+username+"'"+" already exist";
        this.timestamp = new Date();
    }
}
