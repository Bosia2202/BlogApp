package com.denisvasilenko.BlogApp.exceptions.Cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class ArticleDoesntCanReadRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public ArticleDoesntCanReadRuntimeException(String message) {
        this.message = "Article doesn't can read!" +
                "\n Mistake: " + message;
        this.timestamp = new Date();
    }
}
