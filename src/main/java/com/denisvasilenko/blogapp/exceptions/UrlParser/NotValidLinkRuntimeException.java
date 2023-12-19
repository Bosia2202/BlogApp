package com.denisvasilenko.blogapp.exceptions.UrlParser;

import lombok.Getter;

import java.util.Date;

@Getter
public class NotValidLinkRuntimeException extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public NotValidLinkRuntimeException(String url) {
        this.message="ERROR: Not valid link exception:"+"'"+url+"'\n" +
                "Example valid link: https://{bucket}.storage.yandexcloud.net/{key}";
        this.timestamp = new Date();
    }
}
