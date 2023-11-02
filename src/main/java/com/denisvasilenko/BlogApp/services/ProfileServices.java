package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.UserDTO;
import com.denisvasilenko.BlogApp.DTO.UserDTORegistration;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.ArticlePresentation;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.repositories.UserRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;


@Service
@Log4j2
public class ProfileServices {
    private YaCloudService yaCloudService;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;


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
    public void updateArticle(Article article,String newText){
        String url=article.getUrl();
        String bucket;
        String key;
        UrlParser urlParser=new UrlParser(url);
        bucket=urlParser.getBucket();
        key=urlParser.getKey();
        yaCloudService.uploadFiles(bucket,key,newText);
    }

     public List<ArticlePresentation> getAllArticle(){
        List<ArticlePresentation> listArticlePresentation=new LinkedList<>();
        List<Article> listArticle=articleRepository.findAll();
         for (Article tempArticle : listArticle) {
             listArticlePresentation.add(new ArticlePresentation(tempArticle.getNameArticle(),
                     yaCloudService.getArticleText(tempArticle.getUrl()),
                     tempArticle.getUserOwner().getUsername()));
         }
      return listArticlePresentation;
    }
    public void deleteArticle(Article article){
        articleRepository.deleteById(article.getId());
    }

    public UserDTO convertUserToDTO(User user){
        UserDTO userDTO =new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setAvatarImg(user.getAvatarImg());
        userDTO.setProfileDescription(user.getProfileDescription());
        return userDTO;
    }
    public User convertUserDtoRegistrationToUser(UserDTORegistration userDTORegistration){
        return new User((userDTORegistration.getUsername()),
                userDTORegistration.getPassword(),
                null,
                null,
                null);
    }
}
