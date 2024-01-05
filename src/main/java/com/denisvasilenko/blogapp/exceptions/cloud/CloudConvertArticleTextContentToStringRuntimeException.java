package com.denisvasilenko.blogapp.exceptions.cloud;

import lombok.Getter;

import java.util.Date;
@Getter
public class CloudConvertArticleTextContentToStringRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public CloudConvertArticleTextContentToStringRuntimeException(String message) {
        this.message = "ERROR: Didnt happen to convert article content to String\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }
}
