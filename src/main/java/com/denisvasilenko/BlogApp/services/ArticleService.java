package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.exceptions.NotFoundArticleException;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ArticleService {
    private ArticleRepository articleRepository;
    private ProfileServices profileServices;
    private YaCloudService yaCloudService;

    public ArticleDto showArticle(String username,String articleName) {
            if (checkArticleByUser(username, articleName)) {
                Article article = articleRepository.findByNameArticle(articleName).get();
                ArticleDtoMapper articleDtoMapper = new ArticleDtoMapper();
                return articleDtoMapper.getTextFromYandexCloud(article);
            }
            else throw new NotFoundArticleException("Article doesn't exist");
    }

    private boolean checkArticleByUser(String username,String articleName){
        if(profileServices.checkUserExist(username)){
            User user=profileServices.findUserByUserName(username).get();
            List<Article> articlesByUser = user.getArticles();
            for(Article article:articlesByUser){
                if(article.getNameArticle().equals(articleName))
                    return true;
            }
            return false;
        }
        else
            return false;
    }
    public boolean addArticle(String username,String text,String articleName){
        Optional<User> userOptional=profileServices.findUserByUserName(username);
        if(userOptional.isPresent()) {
            User user=userOptional.get();
            String userName=user.getUsername();
            UUID uuid=UUID.randomUUID();
            String articleIdentifier= uuid +userName;
            String urlByArticle=yaCloudService.uploadFiles("blogapp",articleIdentifier,text);
            Article article=new Article();
            article.setUrl(urlByArticle);
            article.setNameArticle(articleName);
            article.setDateOfCreation(getCurrentDate());
            article.setLikes(0);
            article.setUserOwner(user);
            articleRepository.save(article);
            log.info("Article "+articleName+" by "+userName + " added in database");
            return true;
        }
        else {
            log.info("User not exist");
            return false;
        }
    }
    private Date getCurrentDate(){
        Long mills=System.currentTimeMillis();
        return new Date(mills);
    }
    public void updateArticle(Article article,String newText){
        String url=article.getUrl();
        String bucket;
        String key;
        UrlParser urlParser=new UrlParser(url);
        bucket=urlParser.getBucket();
        key=urlParser.getKey();
        yaCloudService.uploadFiles(bucket,key,newText);
    }
    public void deleteArticle(Article article){
        articleRepository.deleteById(article.getId());
    }



}
