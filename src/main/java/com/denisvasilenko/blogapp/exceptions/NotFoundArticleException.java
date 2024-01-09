package com.denisvasilenko.blogapp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundArticleException extends RuntimeException {
        private String articleName;
        private String message;
        private Date timestamp;

        public NotFoundArticleException(UUID id) {
            message="Article with id = "+"'"+id+"'"+" doesn't found";
            this.timestamp = new Date();
        }

        public NotFoundArticleException(String articleName) {
            message="Article "+"'"+articleName+"'"+" doesn't found";
            this.timestamp = new Date();
        }
}

