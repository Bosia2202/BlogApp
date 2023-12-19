package com.denisvasilenko.blogapp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessException extends RuntimeException{
    private String message;
    private Date timestamp;

    public AccessException() {
        message="AccessError";
        this.timestamp = new Date();
    }
}
