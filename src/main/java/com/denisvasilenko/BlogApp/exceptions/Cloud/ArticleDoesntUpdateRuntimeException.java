package com.denisvasilenko.BlogApp.exceptions.Cloud;

import java.util.Date;

public class ArticleDoesntUpdateRuntimeException extends RuntimeException{
    private String articleName;
    private String message;
    private Date timestamp;

    public ArticleDoesntUpdateRuntimeException(String message) {
        this.message = "ERROR: Article doesn't update;\n" +"Mistake: "+ message;
        this.timestamp = new Date();
    }

}
