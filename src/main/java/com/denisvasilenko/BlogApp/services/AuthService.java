package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.JwtRequest;
import com.denisvasilenko.BlogApp.DTO.JwtResponse;
import com.denisvasilenko.BlogApp.DTO.RegistrationUserDto;
import com.denisvasilenko.BlogApp.DTO.UserRegistrationResponse;
import com.denisvasilenko.BlogApp.exceptions.AppError;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.utills.JwtTokenUtills;
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
    private final JwtTokenUtills jwtTokenUtills;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(ProfileServices profileServices, JwtTokenUtills jwtTokenUtills, AuthenticationManager authenticationManager) {
        this.profileServices = profileServices;
        this.jwtTokenUtills = jwtTokenUtills;
        this.authenticationManager = authenticationManager;
    }

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

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        if(!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),"Uncorect password"),HttpStatus.BAD_REQUEST);
        }
        if(profileServices.findUserByUserName(registrationUserDto.getUsername()).isPresent()){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),"Uncorect password"),HttpStatus.BAD_REQUEST);
        }
        User user=profileServices.createUser(registrationUserDto);
        return ResponseEntity.ok(new UserRegistrationResponse(user.getId(),user.getUsername()));
    }
}
