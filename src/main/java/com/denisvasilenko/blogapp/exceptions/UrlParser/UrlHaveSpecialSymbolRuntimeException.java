package com.denisvasilenko.blogapp.exceptions.UrlParser;

import lombok.Getter;

import java.util.Date;
@Getter
public class UrlHaveSpecialSymbolRuntimeException extends RuntimeException {
    private final String message;
    private final Date timestamp;

    public UrlHaveSpecialSymbolRuntimeException(String url) {
        this.message="ERROR: Url have a special symbols in the buket or Key"+"'"+url+"'\n" +
                "Example link structure: https://{bucket}.storage.yandexcloud.net/{key}\"";
        this.timestamp = new Date();
    }
}
