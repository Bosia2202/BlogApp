package com.denisvasilenko.blogapp.controlers;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.denisvasilenko.blogapp.DTO.SearchQueryDto;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoUpdateDTO;
import com.denisvasilenko.blogapp.exceptions.AppError;
import com.denisvasilenko.blogapp.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {
   private final ProfileServices profileServices;
    @Autowired
    public UserController(ProfileServices profileServices) {
        this.profileServices = profileServices;
    }
    @GetMapping("/admin")
    public String admin(){
        return "This is admin page";
    }
    @GetMapping("/test")
    public String test(){
        return "test";
    }
    @GetMapping("/logout")
    public String exit(){
        return "You are exited";
    }

    @GetMapping("/profile/{userName}")
    public ResponseEntity <UserInfoDto> userInfo(@PathVariable ("userName") String userName) {
        UserInfoDto userInfo = profileServices.userInfo(userName);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity <UserInfoDto> userInfo(Principal principal) {
        UserInfoDto userInfo = profileServices.userInfo(principal.getName());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PatchMapping("/info")
    public ResponseEntity<String> updateUser(Principal principal, @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        profileServices.updateUser(principal.getName(), userInfoUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/info")
    public ResponseEntity<String> deleteUser(Principal principal) {
        profileServices.deleteUserByUsername(principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
