package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.exceptions.ExceptionDto;
import com.denisvasilenko.BlogApp.exceptions.NotFoundArticleException;
import com.denisvasilenko.BlogApp.services.ArticleService;
import com.denisvasilenko.BlogApp.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ArticleController {
    private final ProfileServices profileServices;
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ProfileServices profileServices,ArticleService articleService) {
        this.profileServices = profileServices;
        this.articleService=articleService;
    }

    @GetMapping("/{username}/{articleName}") //TODO: Сделать отображение статьи.
    public ArticleDto showCurrentArticle(@PathVariable String username,@PathVariable String articleName)  {
           return articleService.showArticle(username,articleName);
    }



    @PutMapping("/newPost")
    public ResponseEntity<?> createNewPost(Principal principal) { //TODO: Реализовать метод создания статьи.

    }

    @PatchMapping("/{username}/{articleName}")
    public ResponseEntity<?> patchPost (@RequestBody String username,@RequestBody String articleName,Principal principal){ //TODO: Реализовать метод обновления статьи.

    }

    @DeleteMapping("/{username}/{articleName}")
    public ResponseEntity<?> deletePost (@RequestBody String username,@RequestBody String articleName,Principal principal){ //TODO: Реализовать метод удаления статьи.

    }
    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundArticleExceptionResponseEntity(NotFoundArticleException exception){
        ExceptionDto response=new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}