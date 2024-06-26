package com.denisvasilenko.blogapp.exceptions.cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class CloudArticleTextDoesntCanReadRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public CloudArticleTextDoesntCanReadRuntimeException(String message) {
        this.message = "Article doesn't can read!" +
                "\n Mistake: " + message;
        this.timestamp = new Date();
    }
}
