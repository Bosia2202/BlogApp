package com.denisvasilenko.blogapp.DTO.UserDto;

import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDto;
import com.denisvasilenko.blogapp.DTO.ArticleDto.ArticleDtoPreview;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record UserInfoDto(
    byte[] avatarImg,
    String username,
    String profileDescription,
    List<ArticleDtoPreview> articles) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto that = (UserInfoDto) o;
        return Arrays.equals(avatarImg, that.avatarImg) && Objects.equals(username, that.username) && Objects.equals(profileDescription, that.profileDescription) && Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, profileDescription, articles);
        result = 31 * result + Arrays.hashCode(avatarImg);
        return result;
    }

    @Override
    public String toString() {
        return "UserInfoDto {" +
                "avatarImg = " + Arrays.toString(avatarImg) +
                ", username = '" + username + '\'' +
                ", profileDescription = '" + profileDescription + '\'' +
                ", articles = " + articles +
                '}';
    }
}
