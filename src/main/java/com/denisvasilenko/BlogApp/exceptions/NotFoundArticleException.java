package com.denisvasilenko.BlogApp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundArticleException extends RuntimeException {
        private String message;
        private Date timestamp;

        public NotFoundArticleException(String message) {
            this.message = message;
            this.timestamp = new Date();
        }
}

