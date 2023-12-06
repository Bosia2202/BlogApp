package com.denisvasilenko.BlogApp.yandexCloudStore;
import com.denisvasilenko.BlogApp.exceptions.UrlParser.IncorrectURLException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class UrlParser {
        private final String bucket;
        private final String key;
public UrlParser(String url) {
    try {
        URI uri = new URI(url);
        String host = uri.getHost();
        String path = uri.getPath();
        //TODO: Сделать проверку на правильный домен ссылки: yandex.cloud и т.д.
        if (host == null || path == null) {
            throw new IncorrectURLException(url);
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] splitHost = host.split("\\.");
        this.bucket = splitHost[0];
        this.key = path;
    } catch (URISyntaxException e) {
        throw new IncorrectURLException(url);
    }
}
    public String getBucket() {
        return bucket;
    }
    public String getKey() {return key;}
}

