package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.DTO.JWTDto.JwtRequest;
import com.denisvasilenko.BlogApp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.BlogApp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
   private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
      return authService.createAuthToken(authRequest);
    }
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest){
     return authService.createNewUser(userRegistrationRequest);
    }
}
