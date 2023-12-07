package com.denisvasilenko.BlogApp.exceptions.Cloud;


import lombok.Getter;

import java.util.Date;

@Getter
public class ArticleDoesntCreatedRuntimeExceptions extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public ArticleDoesntCreatedRuntimeExceptions(String articleName, String message) {
        this.message = "Article "+"'"+articleName+"'"+" doesn't create" +
                "\n Mistake: " + message;
        this.timestamp = new Date();
    }
}
