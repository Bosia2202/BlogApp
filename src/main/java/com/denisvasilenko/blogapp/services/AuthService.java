package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.dto.jwtdto.JwtRequest;
import com.denisvasilenko.blogapp.dto.jwtdto.JwtResponse;
import com.denisvasilenko.blogapp.dto.registrationdto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.dto.registrationdto.UserRegistrationResponse;
import com.denisvasilenko.blogapp.exceptions.AppError;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.utills.JwtTokenUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public AuthService(ProfileServices profileServices, JwtTokenUtill jwtTokenUtill,
            AuthenticationManager authenticationManager) {
        this.profileServices = profileServices;
        this.jwtTokenUtill = jwtTokenUtill;
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppError("Uncorrected login or password", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = profileServices.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtill.generateToken(userDetails);
        return new JwtResponse(token);
    }

    public UserRegistrationResponse createNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        User user = profileServices.createUser(userRegistrationRequest);
        return new UserRegistrationResponse(user.getId(), user.getUsername());
    }
}
