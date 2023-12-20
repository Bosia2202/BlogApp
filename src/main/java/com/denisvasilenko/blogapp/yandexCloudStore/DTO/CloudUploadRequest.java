package com.denisvasilenko.blogapp.yandexCloudStore.DTO;

import com.denisvasilenko.blogapp.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CloudUploadRequest {
    private String bucketName;
    private String key;
    private String articleName;
    private String articleContent;
    private User userOwner;
    private LocalDate dateOfCreation;
    private final int likes = 0;
}
