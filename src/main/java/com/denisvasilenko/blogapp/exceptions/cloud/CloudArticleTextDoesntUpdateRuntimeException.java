package com.denisvasilenko.blogapp.exceptions.cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class CloudArticleTextDoesntUpdateRuntimeException extends RuntimeException{
    private final String message;
    private final Date timestamp;

    public CloudArticleTextDoesntUpdateRuntimeException(String message) {
        this.message = "ERROR: Article doesn't update;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }

}
