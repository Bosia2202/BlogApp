package com.denisvasilenko.BlogApp.yandexCloudStore;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlParser {
        private String bucket;
        private String key;

        public void parseS3Url(String s3Url) throws URISyntaxException {
            URI uri = new URI(s3Url);
            String host = uri.getHost();
            String path = uri.getPath();
            if (host == null || path == null) {
                throw new URISyntaxException(s3Url, "Invalid S3 URL");
            }
            // Remove the leading '/'
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            String[] splitHost=host.split("\\.");
            this.bucket = splitHost[0];
            this.key = path;
        }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }
}
