package com.denisvasilenko.blogapp.exceptions.userException;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResetPasswordException extends RuntimeException {

    private final String message;
    private final Date timestamp;

    public ResetPasswordException() {
        message = "Password wasn't change. Please check your password and try again";
        this.timestamp = new Date();
    }

}
