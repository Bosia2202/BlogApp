package com.denisvasilenko.blogapp.yandexCloudStore;
import com.denisvasilenko.blogapp.exceptions.urlParser.NotValidLinkRuntimeException;
import com.denisvasilenko.blogapp.exceptions.urlParser.UrlHaveSpecialSymbolRuntimeException;
import lombok.Getter;
import java.net.URI;
import java.net.URISyntaxException;

@Getter
public class UrlParser {
        private String bucket;
        private String key;
public void parseUrl(String url) {
    try {
        checkLinkValidation(url);
        URI uri = new URI(url);
        String host = uri.getHost();
        String path = uri.getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] splitHost = host.split("\\.");
        this.bucket = splitHost[0];
        this.key = path;
    } catch (URISyntaxException e) {
        throw new UrlHaveSpecialSymbolRuntimeException(url);
    }
}
private void checkLinkValidation(String url) {
    String urlRegex = "^https://.*\\.storage\\.yandexcloud\\.net/.*$";
    if(url==null||!url.matches(urlRegex)) {
        throw new NotValidLinkRuntimeException(url);
    }
}
}

