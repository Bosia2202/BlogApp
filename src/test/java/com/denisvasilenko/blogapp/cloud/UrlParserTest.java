package com.denisvasilenko.blogapp.cloud;

import com.denisvasilenko.blogapp.exceptions.urlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.blogapp.exceptions.urlParser.UrlHaveSpecialSymbolRuntimeException;
import com.denisvasilenko.blogapp.cloudStore.UrlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest
public class UrlParserTest {

    @Test
    public void whenPassCorrectUrl_thenGetBucketAndKey() {
        String correctUrl = "https://blogapp.storage.yandexcloud.net/FFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
        String expectedBucket = "blogapp";
        String expectedKey = "FFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF";
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
