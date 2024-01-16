package com.denisvasilenko.blogapp.exceptions.cloud;

import lombok.Getter;

import java.util.Date;

@Getter
public class CloudArticleTextDoesntDeleteRuntimeException extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public CloudArticleTextDoesntDeleteRuntimeException(String message) {
        this.message = "ERROR: Article doesn't delete;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }
}
