package com.denisvasilenko.BlogApp.DTO;

import com.denisvasilenko.BlogApp.models.User;

public class ArticleDto {
    private String userName;
    private String nameArticle;
    private String text;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        userName = userName;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
