package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.DTO.UserDTO;
import com.denisvasilenko.BlogApp.DTO.UserDTORegistration;
import com.denisvasilenko.BlogApp.models.ArticlePresentation;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class BlogController {
   private final ProfileServices profileServices;
    @Autowired
    public BlogController(ProfileServices profileServices) {
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
    @GetMapping("/feed")
   public List<ArticlePresentation> feed() {
        return profileServices.getAllArticle();
    }
//    @GetMapping("/profile/{userName}")
//    public UserDTO userInfo(@PathVariable ("userName") String userName){
//        return profileServices.convertUserToDTO(profileServices.findUserByUserName(userName));
//    }
    @PostMapping("/registration")
    public ResponseEntity<String> createNewUser(@RequestBody UserDTORegistration userDTORegistration){
        System.out.println("Starting create user method");
        User newUser=profileServices.convertUserDtoRegistrationToUser(userDTORegistration);
        System.out.println(newUser.toString());
        profileServices.createUser(newUser);
        return ResponseEntity.ok("Person created successfully");
    }

    @GetMapping("/info")
    public String userInfo(Principal principal){
        return principal.getName();
    }

}
