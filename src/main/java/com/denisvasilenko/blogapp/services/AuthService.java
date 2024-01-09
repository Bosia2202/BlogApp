package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.JWTDto.JwtRequest;
import com.denisvasilenko.blogapp.DTO.JWTDto.JwtResponse;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationResponse;
import com.denisvasilenko.blogapp.exceptions.AppError;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.utills.JwtTokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final ProfileServices profileServices;
    private final JwtTokenUtill jwtTokenUtill;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(ProfileServices profileServices, JwtTokenUtill jwtTokenUtill, AuthenticationManager authenticationManager) {
        this.profileServices = profileServices;
        this.jwtTokenUtill = jwtTokenUtill;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),"Uncorrected login or password"),HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = profileServices.loadUserByUsername(authRequest.getUsername());
        String token= jwtTokenUtill.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest){
        User user = profileServices.createUser(userRegistrationRequest);
        return ResponseEntity.ok(new UserRegistrationResponse(user.getId(),user.getUsername()));
    }
}
