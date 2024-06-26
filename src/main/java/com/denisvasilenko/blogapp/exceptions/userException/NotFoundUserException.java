package com.denisvasilenko.blogapp.exceptions.userException;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundUserException extends RuntimeException {

        private final String message;
        private final Date timestamp;

        public NotFoundUserException(Long id) {
        message="User with id = "+"'" + id.toString() +"'" + " doesn't found";
        this.timestamp = new Date();
        }
        public NotFoundUserException(String username) {
            message="User "+"'" + username+"'" + " doesn't found";
            this.timestamp = new Date();
        }


}
