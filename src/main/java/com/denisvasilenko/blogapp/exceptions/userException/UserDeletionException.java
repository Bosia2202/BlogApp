package com.denisvasilenko.blogapp.exceptions.userException;

import lombok.Getter;

import java.util.Date;

@Getter
public class UserDeletionException extends RuntimeException {
    private String message;
    private final Date timestamp;

    public UserDeletionException(String username, String message) {
        message = "User " + "'" + username+"'" + " cannot be deleted!\n" + "Message: " + message;
        this.timestamp = new Date();
    }
}
