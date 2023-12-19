package com.denisvasilenko.blogapp.DTO.UserDto;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;

import java.util.List;

public record UserInfoDto(
    byte[] avatarImg,
    String username,
    String profileDescription,
    List<ArticleDto> articles) {

}
