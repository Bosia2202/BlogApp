package com.denisvasilenko.blogapp.exceptions;

import java.util.Date;

public record ExceptionDto(
        String message,
        Date timestamp)
{
}
