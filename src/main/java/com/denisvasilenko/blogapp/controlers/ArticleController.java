package com.denisvasilenko.blogapp.controlers;

import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.exceptions.AccessException;
import com.denisvasilenko.blogapp.exceptions.ExceptionDto;
import com.denisvasilenko.blogapp.exceptions.articleException.NotFoundArticleException;
import com.denisvasilenko.blogapp.exceptions.userException.NotFoundUserException;
import com.denisvasilenko.blogapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.UUID;

@RestController
public class ArticleController {
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

//    @GetMapping("/{author}/{articleName}")
//    public ResponseEntity<ArticleDto> showCurrentArticle(@PathVariable String author,@PathVariable String articleName)  {
//          ArticleDto articleDto=articleService.showArticle(author,articleName);
//          return new ResponseEntity<>(articleDto,HttpStatus.OK);
//    }

    @PutMapping("/newPost")
    public ResponseEntity<String> createNewPost(@RequestBody CreateArticleDto createArticleDto, Principal principal) {
        return articleService.addArticle(principal.getName(), createArticleDto);
    }

//    @PatchMapping("/{author}/{articleName}")
//    public ResponseEntity<String> patchPost (@PathVariable String author, @PathVariable String articleName, @RequestBody UpdateArticleDto updateArticleDto, Principal principal) {
//        return articleService.updateArticle(author,principal.getName(),articleName,updateArticleDto);
//    }
//    TODO:Снять скобки

    @DeleteMapping("/{author}/{articleName}")
    public ResponseEntity<String> deletePost (@PathVariable UUID articleId, Principal principal){
        return articleService.deleteArticle(principal.getName(),articleId);
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