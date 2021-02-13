package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidDepthException;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class Crawler {

    final static int DEFAULT_DEPTH = 3;
    final Map<String, Set<String>> crawledPages;

    public Crawler() {
         this.crawledPages = new TreeMap<String, Set<String>>();
    }

    public void crawl(String url, int depth) {
        if( depth<0 ) {
            throw new InvalidDepthException("depth cannot be a negative number");
        }
    }
}
