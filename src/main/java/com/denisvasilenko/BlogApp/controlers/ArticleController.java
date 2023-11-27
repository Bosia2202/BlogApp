package com.denisvasilenko.BlogApp.controlers;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.BlogApp.exceptions.AccessException;
import com.denisvasilenko.BlogApp.exceptions.ExceptionDto;
import com.denisvasilenko.BlogApp.exceptions.NotFoundArticleException;
import com.denisvasilenko.BlogApp.exceptions.NotFoundUserException;
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

    @GetMapping("/{author}/{articleName}") //TODO: Сделать отображение статьи.
    public ResponseEntity<ArticleDto> showCurrentArticle(@PathVariable String author,@PathVariable String articleName)  {
           ArticleDto articleDto=articleService.showArticle(author,articleName);
           return new ResponseEntity<>(articleDto,HttpStatus.OK);
    }



    @PutMapping("/newPost")
    public ResponseEntity<String> createNewPost(@RequestBody CreateArticleDto createArticleDto, Principal principal) { //TODO: Реализовать метод создания статьи.
        return articleService.addArticle(principal.getName(),createArticleDto);
    }

    @PatchMapping("/{author}/{articleName}")
    public ResponseEntity<?> patchPost (@PathVariable String author,@PathVariable String articleName,@RequestBody String newText,Principal principal){ //TODO: Реализовать метод обновления статьи.
        return articleService.updateArticle(author,principal.getName(),articleName,newText);
    }

    @DeleteMapping("/{author}/{articleName}")
    public ResponseEntity<?> deletePost (@PathVariable String author,@PathVariable String articleName,Principal principal){ //TODO: Реализовать метод удаления статьи.
        return articleService.deleteArticle(author,principal.getName(),articleName);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundArticleExceptionResponseEntity(NotFoundArticleException exception){
        ExceptionDto response=new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundUserExceptionResponseEntity(NotFoundUserException exception){
        ExceptionDto response=new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundUserExceptionResponseEntity(AccessException exception){
        ExceptionDto response=new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}