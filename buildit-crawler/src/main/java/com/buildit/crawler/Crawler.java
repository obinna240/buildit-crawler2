package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidCrawlURLException;
import com.buildit.crawler.exceptions.InvalidDepthException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;

public final class Crawler {

    private final static int DEFAULT_DEPTH = 5;
    private final Map<String, Set<String>> indexMap;
    private final Set<String> urlIndex;
    private final static String HTML_ANCHOR_REF = "a[href]";
    private final static String HTML_IMG_REF = "img";
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);
    final BiPredicate<String, String> COMPARE_DOMAIN = (URI1, URI2) -> compareDomainName(URI1, URI2);

    public Crawler() {
         this.indexMap = new LinkedHashMap<>();
         this.urlIndex = new HashSet<>();
    }

    //default method to crawl
    public Map crawl(String url){
        return crawl(url, 0);
    }

    public Map crawl(String url, int crawlDepth) {
        if (!validateUrl(url)) {
            throw new InvalidCrawlURLException("The URL is invalid");
        }
        if( crawlDepth < 0 || crawlDepth > 5) {
            throw new InvalidDepthException("depth cannot be a negative number");
        }
        if(!urlIndex.contains(url) && crawlDepth < DEFAULT_DEPTH) {
            try {
                urlIndex.add(url);
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select(HTML_ANCHOR_REF);
                Elements imageLinks = doc.select(HTML_IMG_REF);
                links.addAll(imageLinks);
                int finalCrawlDepth = crawlDepth++;

                links.stream().forEach(link -> {
                    populateIndex(indexMap, url, link);
                    writeJsonSiteMap(indexMap);

                    if(COMPARE_DOMAIN.test(url, link.attr("abs:href"))) {
//                        String linkToCrawl = StringUtils.startsWith(link.attr("abs:href"), "/") ? url+link.attr("abs:href") : link.attr("abs:href");
                        crawl(link.attr("abs:href"), finalCrawlDepth);
                    }

                });
            }
            catch(Exception ex) {
                LOGGER.info(ex.getMessage());
            }
        }
        return indexMap;
    }

    /**
     * Write index in a file in classpath
     * @param map
     */
    private void writeJsonSiteMap(Map map) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(Paths.get("site-map.json").toFile(), map);
        }
        catch(Exception ex) {
            LOGGER.info(ex.getMessage());
        }
    }

    /**
     * Populate index
     * @param index
     * @param url
     * @param element
     */
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
            if (StringUtils.isNotBlank(href)) {
                index_set.add(href);
            }
            if (StringUtils.isNotBlank(imgsrc)) {
                index_set.add(imgsrc);
            }
            index.put(url, index_set);
        }
    }

    /**
     * Validate url
     * @param url
     * @return
     */
    private boolean validateUrl(String url) {
        Objects.requireNonNull(url, "The url cannot be null");
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    /**
     * Compare Domain names to exclude externa links
     * @param url1
     * @param url2
     * @return
     */
    private boolean compareDomainName(String url1, String url2) {
        try {
            //for internal urls
            if(StringUtils.startsWith(url2, "/")) {
                return true;
            }

            URI uri = new URI(url1);
            String domain = uri.getHost();
            String domainSubString1 = domain.startsWith("www.") ? domain.substring(4) : domain;
            String domainName = StringUtils.substringBefore(domainSubString1, ".");

            URI uri2 = new URI(url2);
            String domain2 = uri2.getHost();
            String domainSubstring2 = domain2.startsWith("www.") ? domain2.substring(4) : domain2;
            String domainName2 = StringUtils.substringBefore(domainSubstring2, ".");

            return domainName.equalsIgnoreCase(domainName2);
        }
        catch(Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return false;
    }
}
