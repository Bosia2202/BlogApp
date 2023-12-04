package com.denisvasilenko.BlogApp.yandexCloudStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class UrlParser {
        private String bucket;
        private String key;
public UrlParser(String url) {
    try {
        URI uri = new URI(url);
        String host = uri.getHost();
        String path = uri.getPath();
        if (host == null || path == null) {
            throw new URISyntaxException(url, "Invalid S3 URL");
        }
        // Remove the leading '/'
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] splitHost = host.split("\\.");
        this.bucket = splitHost[0];
        this.key = path;
    } catch (URISyntaxException e) {
        throw new RuntimeException(e);
    }
}
    public String getBucket() {
        return bucket;
    }
    public Optional<String> getKey() {
        if(key.matches("")) {
            return Optional.empty();
        }
        else {
            return Optional.of(key);
        }
    }
}
