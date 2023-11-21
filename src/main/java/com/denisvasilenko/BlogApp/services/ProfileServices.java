package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.BlogApp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.BlogApp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.BlogApp.config.PasswordEncoderConfig;
import com.denisvasilenko.BlogApp.exceptions.AppError;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.repositories.UserRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class ProfileServices implements UserDetailsService {
    private final YaCloudService yaCloudService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final RoleService roleService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    public ProfileServices(YaCloudService yaCloudService, UserRepository userRepository, ArticleRepository articleRepository,RoleService roleService,PasswordEncoderConfig passwordEncoderConfig) {
        this.yaCloudService = yaCloudService;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.roleService=roleService;
        this.passwordEncoderConfig=passwordEncoderConfig;
    }

    @Transactional
    public User createUser(UserRegistrationRequest userRegistrationRequest){
        User user=new User();
        user.setUsername(userRegistrationRequest.getUsername());
        user.setPassword(passwordEncoderConfig.beanpasswordEncoder().encode(userRegistrationRequest.getPassword()));
        user.setRoleCollection(List.of(roleService.getUserRole()));
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
    public Optional<User> findUserByUserName(String name){
      return userRepository.findByUsername(name);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=findUserByUserName(username).orElseThrow(()->new UsernameNotFoundException(
               String.format("User '%s' doesn't found", username)
       ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoleCollection().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())

        );
    }

    public UserInfoDto userInfo(String username) throws AppError {
        if (findUserByUserName(username).isPresent()){
            User user=findUserByUserName(username).get();
            return new UserInfoDto(user.getAvatarImg(),
                    user.getUsername(),
                    user.getProfileDescription(),
                    getAllArticlesByUser(user));
        }
        else {
            throw new AppError(404, "User not found");
        }
    }

    public List<ArticleDto> getAllArticlesByUser(User user){
        TextGetter textGetter =new TextGetter();
        return user.getArticles().stream().map(textGetter::getTextFromYandexCloud)
                .collect(Collectors.toList());
    }
    public boolean addArticle(Long id,String text,String articleName){
        Optional<User> userOptional=userRepository.findById(id);
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
