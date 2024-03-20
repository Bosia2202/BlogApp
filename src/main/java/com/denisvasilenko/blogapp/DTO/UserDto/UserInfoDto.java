package com.denisvasilenko.blogapp.DTO.UserDto;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDtoPreview;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record UserInfoDto(
    String username,
    String profileDescription,
    List<ArticleDtoPreview> articles) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto that = (UserInfoDto) o;
        return Objects.equals(username, that.username) && Objects.equals(profileDescription, that.profileDescription) && Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, profileDescription, articles);
    }

    @Override
    public String toString() {
        return "UserInfoDto {" +
                ", username = '" + username + '\'' +
                ", profileDescription = '" + profileDescription + '\'' +
                ", articles = " + articles +
                '}';
    }
}
