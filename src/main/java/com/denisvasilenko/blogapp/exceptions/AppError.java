package com.denisvasilenko.blogapp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data

public class AppError extends RuntimeException{
    private HttpStatus status;
    private String message;
    private Date timestamp;

    public AppError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = new Date();
    }
}
