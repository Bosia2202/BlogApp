package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.models.ArticlePresentation;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.services.ProfileServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogController {
   private final ProfileServices profileServices;

    public BlogController(ProfileServices profileServices) {
        this.profileServices = profileServices;
    }

    @GetMapping("/feed")
   public List<ArticlePresentation> FeedContoller() {
        return profileServices.getAllArticle();
    }
    @GetMapping("/{userName}")
    public User user(@PathVariable ("userName") String userName){
       profileServices.findUserByUserName() //TODO
    }
}
