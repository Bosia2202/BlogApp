package com.denisvasilenko.BlogApp.exceptions.UrlParser;

import java.util.Date;

public class IncorrectURLException extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public IncorrectURLException(String url) {
        this.message="ERROR: Incorrect URL:"+"'"+url+"'";
        this.timestamp = new Date();
    }
}
