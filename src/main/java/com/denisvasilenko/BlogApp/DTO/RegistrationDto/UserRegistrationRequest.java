package com.denisvasilenko.BlogApp.DTO.RegistrationDto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
