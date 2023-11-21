package com.denisvasilenko.BlogApp.DTO.UserDto;

import com.denisvasilenko.BlogApp.DTO.ArticleDto.ArticleDto;

import java.util.List;

public record UserInfoDto(
    byte[] avatarImg,
    String username,
    String profileDescription,
    List<ArticleDto> articles) {

}
