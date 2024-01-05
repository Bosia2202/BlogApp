package com.denisvasilenko.blogapp.exceptions.cloud;

import java.util.Date;

public class CloudArticleTextDoesntDeleteRuntimeException extends RuntimeException {
    private String articleName;
    private String message;
    private Date timestamp;

    public CloudArticleTextDoesntDeleteRuntimeException(String message) {
        this.message = "ERROR: Article doesn't delete;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }
}
