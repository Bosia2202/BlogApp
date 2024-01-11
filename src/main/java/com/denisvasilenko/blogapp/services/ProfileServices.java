package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoUpdateDTO;
import com.denisvasilenko.blogapp.config.PasswordEncoderConfig;
import com.denisvasilenko.blogapp.exceptions.userException.NotFoundUserException;
import com.denisvasilenko.blogapp.exceptions.userException.UserAlreadyExist;
import com.denisvasilenko.blogapp.models.Article;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Log4j2
public class ProfileServices implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    public ProfileServices(UserRepository userRepository,RoleService roleService,PasswordEncoderConfig passwordEncoderConfig) {
        this.userRepository = userRepository;
        this.roleService=roleService;
        this.passwordEncoderConfig=passwordEncoderConfig;
    }

    @Transactional
    public User createUser(UserRegistrationRequest userRegistrationRequest){
        try {
        User user = new User();
        user.setUsername(userRegistrationRequest.username());
        user.setPassword(passwordEncoderConfig.beanpasswordEncoder().encode(userRegistrationRequest.password()));
        List<Article> articles = new ArrayList<>();
        user.setArticles(articles);
        user.setRoleCollection(List.of(roleService.getUserRole()));
        return userRepository.save(user);
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new UserAlreadyExist(userRegistrationRequest.username());
        }
    }

    public User refreshUserData(User user) {
        return findUserById(user.getId()); //TODO: ИСПРАВИТЬ ОШИБКИ КОТОРЫЕ В SONARLIST
    }

    @Transactional
    public User updateUser(User oldUser, UserInfoUpdateDTO userInfoUpdateDTO)
    {
     User updateUser = oldUser.duplicatingUser();
     if(!oldUser.getPassword().equals(userInfoUpdateDTO.password()) && userInfoUpdateDTO.password()!=null) {
         updateUser.setPassword(passwordEncoderConfig.beanpasswordEncoder().encode(userInfoUpdateDTO.password()));
     }
     if (oldUser.getAvatarImg() == null || !Arrays.equals(oldUser.getAvatarImg(),userInfoUpdateDTO.avatarImg()) && userInfoUpdateDTO.avatarImg() != null) {
         updateUser.setAvatarImg(userInfoUpdateDTO.avatarImg());
     }
     if (oldUser.getProfileDescription() == null|| !oldUser.getProfileDescription().equals(userInfoUpdateDTO.profileDescription()) && userInfoUpdateDTO.profileDescription()!=null) {
         updateUser.setProfileDescription(userInfoUpdateDTO.profileDescription());
     }
     return userRepository.save(updateUser);
    }

    public void deleteUser(User user) {
       userRepository.deleteById(user.getId());
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User findUserById(Long id) {
       User foundedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("User not found"));
       Hibernate.initialize(foundedUser.getArticles());
       entityManager.detach(foundedUser);
       return foundedUser;
    }
    @Transactional
    public User findUserByUserName(String name){
        User foundedUser =  userRepository.findByUsername(name).orElseThrow(() -> new NotFoundUserException("User not found"));
        Hibernate.initialize(foundedUser.getArticles());
        entityManager.detach(foundedUser);
        return foundedUser;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = findUserByUserName(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoleCollection().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }

    public UserInfoDto userInfo(String username) {
            User user = findUserByUserName(username);
            return new UserInfoDto(user.getAvatarImg(),
                    user.getUsername(),
                    user.getProfileDescription(),
                    getAllArticlesByUser(user));
    }
    public List<ArticleDto> getAllArticlesByUser(User user){
        ArticleDtoMapper articleDtoMapper = new ArticleDtoMapper();
        return user.getArticles().stream().map(articleDtoMapper::getTextFromYandexCloud)
                .toList();
    }


}
