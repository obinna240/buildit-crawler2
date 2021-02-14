package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidCrawlURLException;
import com.buildit.crawler.exceptions.InvalidDepthException;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.*;

public final class Crawler {

    final static int DEFAULT_DEPTH = 3;
    final Map<String, Set<String>> crawledPages;

    public Crawler() {
         this.crawledPages = new LinkedHashMap<String, Set<String>>();
    }

    public void crawl(String url, int depth) {
        if (!validateUrl(url)) {
            throw new InvalidCrawlURLException("The URL is invalid");
        }
        if( depth<0 ) {
            throw new InvalidDepthException("depth cannot be a negative number");
        }
    }

    private boolean validateUrl(String url) {
        Objects.requireNonNull(url, "The url cannot be null");
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
