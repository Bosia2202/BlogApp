package com.denisvasilenko.blogapp.exceptions;

import java.util.Date;

public class RoleDoesNotExistException extends Exception {
    private String roleName;
    private String message;
    private Date timestamp;

    public RoleDoesNotExistException(String roleName) {
        message = "Role " + "'" + roleName + "'" + " doesn't exist";
        this.timestamp = new Date();
    }

}
