package com.denisvasilenko.blogapp.DTO.UserDto;

public record UserInfoUpdateDTO(String password, byte[] avatarImg, String profileDescription) {
}
