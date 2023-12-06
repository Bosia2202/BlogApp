package com.denisvasilenko.BlogApp.exceptions.Cloud;

import java.util.Date;

public class ArticleDoesntDeleteRuntimeException extends RuntimeException {
    private String articleName;
    private String message;
    private Date timestamp;

    public ArticleDoesntDeleteRuntimeException(String message) {
        this.message = "ERROR: Article doesn't delete;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }
}
