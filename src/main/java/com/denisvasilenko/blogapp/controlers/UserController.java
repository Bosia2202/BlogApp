package com.denisvasilenko.blogapp.controlers;

import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.blogapp.exceptions.AppError;
import com.denisvasilenko.blogapp.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserInfoDto userInfo(@PathVariable ("userName") String userName) throws AppError {
        return profileServices.userInfo(userName);
    }

    @GetMapping("/info")
    public UserInfoDto userInfo(Principal principal) throws AppError {
        return profileServices.userInfo(principal.getName());
    }

}
