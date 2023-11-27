package com.denisvasilenko.BlogApp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundUserException extends RuntimeException {

        private String username;
        private String message;
        private Date timestamp;

        public NotFoundUserException(String username) {
            message="User "+"'"+username+"'"+" doesn't found";
            this.timestamp = new Date();
        }
}
