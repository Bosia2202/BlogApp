package com.denisvasilenko.BlogApp.Cloud;

import com.denisvasilenko.BlogApp.yandexCloudStore.YaCloudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class YaCloudServiceTest {

    @Autowired
    private YaCloudService yaCloudService;

    @Test
    public void whenWeUploadFile_thanWeShouldToGetExpectedText() {

    }

    @Test
    public void whenWeUploadFileWithWrongKey_thanWeGetNullUrl() {

    }

    @Test
    public void whenWeUploadFileWithoutText_thanWeDoesntShouldToGetText() {

    }
}
