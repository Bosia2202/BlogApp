package com.denisvasilenko.blogapp.exceptions.userException;

import lombok.Getter;

import java.util.Date;

@Getter
public class UserAlreadyExist extends RuntimeException {

    private final String message;
    private final Date timestamp;

    public UserAlreadyExist(String username) {
        message = "User "+"'"+username+"'"+" already exist";
        this.timestamp = new Date();
    }
}
