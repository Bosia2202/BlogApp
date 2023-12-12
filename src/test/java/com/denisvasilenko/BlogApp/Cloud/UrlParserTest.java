package com.denisvasilenko.BlogApp.Cloud;

import com.denisvasilenko.BlogApp.exceptions.UrlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.BlogApp.exceptions.UrlParser.UrlHaveSpecialSymbolRuntimeException;
import com.denisvasilenko.BlogApp.yandexCloudStore.UrlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UrlParserTest {

    @Test
    public void whenPassCorrectUrl_thenGetBucketAndKey() {
        String correctUrl = "https://blogapp.storage.yandexcloud.net/b776b1ca-0e55-4b93-bc93-c6db81bb6436PosterTestArticle";
        String expectedBucket = "blogapp";
        String expectedKey = "b776b1ca-0e55-4b93-bc93-c6db81bb6436PosterTestArticle";
        UrlParser urlParser = new UrlParser();
        urlParser.parseUrl(correctUrl);
        String actualBucket = urlParser.getBucket();
        String actualKey = urlParser.getKey();
        Assertions.assertEquals(expectedBucket,actualBucket);
        Assertions.assertEquals(expectedKey,actualKey);
    }

    @Test
    public void whenPassJustWordInsteadOfUrl_thanGetNotValidLinkRuntimeException() {
        String justWord = "apple";
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->urlParser.parseUrl(justWord));
    }

    @Test
    public void whenPassUrlWithoutHost_thanGetNotValidLinkRuntimeException() {
        String urlWithoutHost = "https://b776b1ca-0e55-4b93-bc93-c6db81bb6436PosterTestArticle";
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->urlParser.parseUrl(urlWithoutHost));
    }

    @Test
    public void whenPassUrlWithoutPath_thanGetNotValidLinkRuntimeException() {
        String urlWithoutPath = "https://blogapp.storage.yandexcloud.net";
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->urlParser.parseUrl(urlWithoutPath));
    }

    @Test
    public void whenPassUrlWithSpecialSymbolsInTheBuket_thanGetUrlHaveSpecialSymbolRuntimeException() {
        String UrlWithSpecialSymbolsInTheBuket = "https://blog^^^app.storage.yandexcloud.net/b776b1ca-0e55-4b93-bc93-c6db81bb6436PosterTestArticle";
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(UrlHaveSpecialSymbolRuntimeException.class,()->urlParser.parseUrl(UrlWithSpecialSymbolsInTheBuket));
    }

    @Test
    public void whenPassUrlWithSpecialSymbolsInTheKey_thanGetUrlHaveSpecialSymbolRuntimeException() {
        String UrlWithSpecialSymbolsInTheKey = "https://blogapp.storage.yandexcloud.net/b776b1ca-0e^55-4b9№3-bc93-c6db81bb6436PosterTestArticle";
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(UrlHaveSpecialSymbolRuntimeException.class,()->urlParser.parseUrl(UrlWithSpecialSymbolsInTheKey));
    }

    @Test
    public void whenPassNullToUrlArgument_thanShouldGetNotValidLinkRuntimeException() {
        UrlParser urlParser = new UrlParser();
        Assertions.assertThrows(NotValidLinkRuntimeException.class,()->urlParser.parseUrl(null));
    }
}
