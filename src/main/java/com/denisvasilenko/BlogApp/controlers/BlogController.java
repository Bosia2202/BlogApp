package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogController {
    YaCloudService yaCloudService = new YaCloudService();
    @GetMapping("/feed")
    public List<String> FeedContoller() {
        return yaCloudService.getAllArticles("blogapp");
    }
}
