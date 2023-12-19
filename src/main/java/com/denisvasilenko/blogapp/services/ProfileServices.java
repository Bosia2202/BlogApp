package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.blogapp.config.PasswordEncoderConfig;
import com.denisvasilenko.blogapp.exceptions.AppError;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class ProfileServices implements UserDetailsService {

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
        ArticleDtoMapper articleDtoMapper =new ArticleDtoMapper();
        return user.getArticles().stream().map(articleDtoMapper::getTextFromYandexCloud)
                .collect(Collectors.toList());
    }



}
