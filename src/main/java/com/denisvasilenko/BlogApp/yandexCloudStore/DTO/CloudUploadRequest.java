package com.denisvasilenko.BlogApp.yandexCloudStore.DTO;

import com.denisvasilenko.BlogApp.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
public class CloudUploadRequest {
    private String bucketName;
    private String key;
    private String articleName;
    private String articleContent;
    private User userOwner;
    private Date DateOfCreation;
    private final int likes=0;
}
