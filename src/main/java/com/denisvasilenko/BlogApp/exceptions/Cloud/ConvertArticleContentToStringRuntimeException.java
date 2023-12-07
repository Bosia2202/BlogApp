package com.denisvasilenko.BlogApp.exceptions.Cloud;

import lombok.Getter;

import java.util.Date;
@Getter
public class ConvertArticleContentToStringRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public ConvertArticleContentToStringRuntimeException(String message) {
        this.message = "ERROR: Didnt happen to convert article content to String\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }
}
