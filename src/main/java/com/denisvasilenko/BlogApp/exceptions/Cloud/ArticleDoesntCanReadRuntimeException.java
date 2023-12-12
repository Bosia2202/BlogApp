package com.denisvasilenko.BlogApp.exceptions.Cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class ArticleDoesntCanReadRuntimeException {
    private final String message;
    private final Date timestamp;

    public ArticleDoesntCanReadRuntimeException(String articleName, String message) {
        this.message = "Article "+"'"+articleName+"'"+" doesn't can read!" +
                "\n Mistake: " + message;
        this.timestamp = new Date();
    }
}
