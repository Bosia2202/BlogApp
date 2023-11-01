package com.denisvasilenko.BlogApp.models;

import java.util.Objects;

public class ArticlePresentation {
    private String nameArticle;
    private String textOfArticle;
    private String author;

    public ArticlePresentation(String nameArticle, String textOfArticle, String author) {
        this.nameArticle = nameArticle;
        this.textOfArticle = textOfArticle;
        this.author = author;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public String getTextOfArticle() {
        return textOfArticle;
    }

    public void setTextOfArticle(String textOfArticle) {
        this.textOfArticle = textOfArticle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlePresentation that = (ArticlePresentation) o;
        return Objects.equals(nameArticle, that.nameArticle) && Objects.equals(textOfArticle, that.textOfArticle) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameArticle, textOfArticle, author);
    }

    @Override
    public String toString() {
        return "ArticlePresentation{" +
                "nameArticle='" + nameArticle + '\'' +
                ", textOfArticle='" + textOfArticle + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
