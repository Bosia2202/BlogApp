package com.denisvasilenko.BlogApp.exceptions.Cloud;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDoesntCreatedRuntimeExceptions extends RuntimeException{
    private String articleName;
    private String message;
    private Date timestamp;

    public ArticleDoesntCreatedRuntimeExceptions(String articleName) {
        message = "Article "+"'"+articleName+"'"+" doesn't create";
        this.timestamp = new Date();
    }
}
