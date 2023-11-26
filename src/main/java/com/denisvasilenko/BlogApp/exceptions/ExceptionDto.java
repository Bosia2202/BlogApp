package com.denisvasilenko.BlogApp.exceptions;

import java.util.Date;

public record ExceptionDto(
        String message,
        Date timestamp)
{
}
