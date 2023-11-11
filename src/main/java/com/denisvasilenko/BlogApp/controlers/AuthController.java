package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.DTO.JwtRequest;
import com.denisvasilenko.BlogApp.DTO.JwtResponse;
import com.denisvasilenko.BlogApp.exceptions.AppError;
import com.denisvasilenko.BlogApp.services.ProfileServices;
import com.denisvasilenko.BlogApp.utills.JwtTokenUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final ProfileServices profileServices;
    private final JwtTokenUtills jwtTokenUtills;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthController(ProfileServices profileServices, JwtTokenUtills jwtTokenUtills, AuthenticationManager authenticationManager) {
        this.profileServices = profileServices;
        this.jwtTokenUtills = jwtTokenUtills;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),"Uncorrected login or password"),HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails=profileServices.loadUserByUsername(authRequest.getUsername());
        String token=jwtTokenUtills.generateToken(userDetails);
        return  ResponseEntity.ok(new JwtResponse(token));
    }
}
