package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.DTO.UserDTO;
import com.denisvasilenko.BlogApp.DTO.JwtRequest;
import com.denisvasilenko.BlogApp.models.Article;
import com.denisvasilenko.BlogApp.models.ArticlePresentation;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.ArticleRepository;
import com.denisvasilenko.BlogApp.repositories.RoleRepository;
import com.denisvasilenko.BlogApp.repositories.UserRepository;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class ProfileServices implements UserDetailsService {
    private final YaCloudService yaCloudService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ProfileServices(YaCloudService yaCloudService, UserRepository userRepository, ArticleRepository articleRepository, RoleRepository roleRepository) {
        this.yaCloudService = yaCloudService;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User createUser(User user){
        user.setRoleCollection(List.of(roleRepository.findByName("ROLE_USER").get()));
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
               String.format("Пользователь '%s' не найден", username)
       ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoleCollection().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())

        );
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
    public User convertUserDtoRegistrationToUser(JwtRequest userDTORegistration){
        return new User((userDTORegistration.getUsername()),
                userDTORegistration.getPassword(),
                null,
                null,
                null);
    }

}
