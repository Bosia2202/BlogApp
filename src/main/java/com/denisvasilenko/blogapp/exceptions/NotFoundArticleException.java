package com.denisvasilenko.blogapp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundArticleException extends RuntimeException {
        private String articleName;
        private String message;
        private Date timestamp;

        public NotFoundArticleException(String articleName) {
            message="Article "+"'"+articleName+"'"+" doesn't found";
            this.timestamp = new Date();
        }
}

