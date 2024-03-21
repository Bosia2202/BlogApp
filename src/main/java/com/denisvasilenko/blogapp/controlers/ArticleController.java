package com.denisvasilenko.blogapp.controlers;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.CreateArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.UpdateArticleDto;
import com.denisvasilenko.blogapp.exceptions.AccessException;
import com.denisvasilenko.blogapp.exceptions.ExceptionDto;
import com.denisvasilenko.blogapp.exceptions.articleException.NotFoundArticleException;
import com.denisvasilenko.blogapp.exceptions.cloud.*;
import com.denisvasilenko.blogapp.exceptions.urlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.blogapp.exceptions.urlParser.UrlHaveSpecialSymbolRuntimeException;
import com.denisvasilenko.blogapp.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RequestMapping("/article")
@RestController
public class ArticleController {
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PutMapping("/newArticle")
    public ResponseEntity<String> createNewArticle(@RequestBody CreateArticleDto createArticleDto, Principal principal) {
        return articleService.addArticle(principal.getName(), createArticleDto);
    }
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDto> showCurrentArticle(@PathVariable String articleId)  {
        ArticleDto articleDto = articleService.showArticle(articleId);
        return new ResponseEntity<>(articleDto,HttpStatus.OK);
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<String> patchArticle (@PathVariable String articleId, @RequestBody UpdateArticleDto updateArticleDto, Principal principal) {
        return articleService.updateArticle(principal.getName(),articleId,updateArticleDto);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle (@PathVariable String articleId, Principal principal){
        return articleService.deleteArticle(principal.getName(),articleId);
    }
    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundArticleExceptionResponseEntity(NotFoundArticleException exception){
        ExceptionDto response=new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<String> cloudArticleTextDoesntCanReadRuntimeExceptionResponseEntity(CloudArticleTextDoesntCanReadRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> cloudArticleTextDoesntCreatedRuntimeExceptions(CloudArticleTextDoesntCreatedRuntimeExceptions exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> cloudArticleTextDoesntDeleteRuntimeException(CloudArticleTextDoesntDeleteRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> cloudArticleTextDoesntUpdateRuntimeException(CloudArticleTextDoesntUpdateRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> cloudConvertArticleTextContentToStringRuntimeException(CloudConvertArticleTextContentToStringRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> notValidLinkRuntimeException(NotValidLinkRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> urlHaveSpecialSymbolRuntimeException(UrlHaveSpecialSymbolRuntimeException exception){
        return new ResponseEntity<>("InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> accessExceptionResponseEntity(AccessException exception){
        ExceptionDto response = new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}