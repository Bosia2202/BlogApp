package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.repositories.UserRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Log4j2
public class ProfileServices {
    private final YaCloudService yaCloudService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ProfileServices(YaCloudService yaCloudService, UserRepository userRepository, ArticleRepository articleRepository) {
        this.yaCloudService = yaCloudService;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User uploadUser(Long id,User user)
    {
      user.setId(id);
      return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User findUserByUserName(String name){
       Optional<User> user = Optional.ofNullable(userRepository.findByUsername(name));
       return user.orElse(null);
    }

    public List<Article> getAllArticlesByUser(User user){
        return user.getArticles();
    }
    public boolean addArticle(Long id,String text,String articleName){
        Optional<User> userOptional =userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user=userOptional.get();
            String userName=user.getUsername();
            UUID uuid=UUID.randomUUID();
            String articleIdentifier=uuid.toString()+userName;
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
    public boolean updateArticle(){
        return true;//TODO
    }
    public boolean deleteArticle(Long id){
        return true;//TODO
    }
}
