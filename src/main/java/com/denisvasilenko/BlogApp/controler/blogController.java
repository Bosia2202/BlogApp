package com.denisvasilenko.BlogApp.controler;

import com.denisvasilenko.BlogApp.yandexCloudStore.cloud;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class blogController {
    @GetMapping("/test")
    public String TestContoller(){
        cloud clouud = new cloud();
        clouud.cloudCreateBacket();
        return "Method end";
    }
}
