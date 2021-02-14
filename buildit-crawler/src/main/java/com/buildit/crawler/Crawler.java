package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidCrawlURLException;
import com.buildit.crawler.exceptions.InvalidDepthException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public final class Crawler {

    private final static int DEFAULT_DEPTH = 3;
    private final Map<String, Set<String>> indexMap;
    private final Set<String> urlIndex;
    private final static String HTML_ANCHOR_REF = "a[href]";
    private final static String HTML_IMG_REF = "img";

    public Crawler() {
         this.indexMap = new LinkedHashMap<>();
         this.urlIndex = new HashSet<>();
    }

    public void crawl(String url, int crawlDepth) {
        if (!validateUrl(url)) {
            throw new InvalidCrawlURLException("The URL is invalid");
        }
        if( crawlDepth<0 ) {
            throw new InvalidDepthException("depth cannot be a negative number");
        }
        if(!urlIndex.contains(url) && crawlDepth < DEFAULT_DEPTH) {
            try {
                urlIndex.add(url);
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select(HTML_ANCHOR_REF);
                Elements imageLinks = doc.select(HTML_IMG_REF);
                links.addAll(imageLinks);
                crawlDepth++;

                links.stream().forEach(link -> {
                    populateIndex(indexMap, url, link);
//                    if(link)
                });
            }
            catch(Exception ex) {

            }
        }
    }

    public void crawl(String url){

    }

    private void populateIndex(Map<String, Set<String>> index, String url, Element element){
        String href = element.attr("abs:href");
        String imgsrc = element.attr("abs:src");
        if(index.containsKey(url)) {
            if (StringUtils.isNotBlank(href)) {
                index.get(url).add(href);
            }
            if (StringUtils.isNotBlank(imgsrc)) {
                index.get(url).add(imgsrc);
            }
        }
        else {
            Set<String> index_set = new LinkedHashSet<>();
            index_set.add(href);
            index_set.add(imgsrc);
            index.put(url, index_set);
        }
    }

    private boolean validateUrl(String url) {
        Objects.requireNonNull(url, "The url cannot be null");
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
