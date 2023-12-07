package com.denisvasilenko.BlogApp.exceptions.Cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class ArticleDoesntUpdateRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public ArticleDoesntUpdateRuntimeException(String message) {
        this.message = "ERROR: Article doesn't update;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }

}
