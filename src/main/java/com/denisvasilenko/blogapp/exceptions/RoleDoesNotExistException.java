package com.denisvasilenko.blogapp.exceptions;

import lombok.Getter;

import java.util.Date;

@Getter
public class RoleDoesNotExistException extends Exception {
    private final String message;
    private final Date timestamp;

    public RoleDoesNotExistException(String roleName) {
        message = "Role " + "'" + roleName + "'" + " doesn't exist";
        this.timestamp = new Date();
    }
}
