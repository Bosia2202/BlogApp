package com.denisvasilenko.blogapp.DTO.UserDto;

public class UserDTO {
    private String username;
    private byte[] avatarImg;
    private String profileDescription;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(byte[] avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }
}
