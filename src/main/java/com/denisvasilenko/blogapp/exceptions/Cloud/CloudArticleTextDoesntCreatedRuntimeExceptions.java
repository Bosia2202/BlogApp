package com.denisvasilenko.blogapp.exceptions.Cloud;


import lombok.Getter;

import java.util.Date;

@Getter
public class CloudArticleTextDoesntCreatedRuntimeExceptions extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public CloudArticleTextDoesntCreatedRuntimeExceptions(String articleName, String message) {
        this.message = "Article "+"'"+articleName+"'"+" doesn't create" +
                "\n Mistake: " + message;
        this.timestamp = new Date();
    }
}
