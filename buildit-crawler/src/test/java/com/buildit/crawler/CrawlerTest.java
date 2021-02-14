package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidCrawlURLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.buildit.crawler.exceptions.InvalidDepthException;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrawlerTest {

    Crawler crawler;

    String testURL1 = "http://www.bbc.co.uk";
    String testURL2 = "http://www.dummy.co.uk";

    @BeforeEach
    void init(){
        crawler = new Crawler();
    }

    @Test
    @DisplayName("Invalid url test")
    public void willReturnFalseForInvalidURL(){
        UrlValidator urlValidator = new UrlValidator();
        boolean isValid = urlValidator.isValid("bbc.co.uk");
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Invalid depth exception")
    public void willReturnExceptionForInvalidDepth(){
        assertThrows(InvalidDepthException.class, () -> {
            crawler.crawl(testURL1, -1);
        });

    }

    @Test
    public void willReturnExceptionForInvalidOrNullURL() {
        assertThrows(InvalidCrawlURLException.class, () -> {
            crawler.crawl("invalidURL", 1);
        });
    }

    @Test
    public void willReturnGoogleAsDomainName() throws Exception {
        String test = "http://www.google.co.uk#1234";
        URI uri = new URI(test);
        String domain = uri.getHost();
        String dd = domain.startsWith("www.")?domain.substring(4): domain;
        String domainName = StringUtils.substringBefore(dd, ".");
        assertEquals("google", domainName);
    }

    @Test
    public void willReturnFalseOnCheckingThatDomainNamesAreNotEqual() throws Exception {
        String url1 = "http://www.google.co.uk#1234";
        String url2 = "https://www.abcnews.com";
        URI uri = new URI(url1);
        String domain = uri.getHost();
        String dd = domain.startsWith("www.")?domain.substring(4): domain;
        String domainName = StringUtils.substringBefore(dd, ".");

        URI uri2 = new URI(url2);
        String domain2 = uri2.getHost();
        String dd2= domain2.startsWith("www.")?domain2.substring(4): domain2;
        String domainName2 = StringUtils.substringBefore(dd2, ".");

        boolean compare = domainName.equalsIgnoreCase(domainName2);
        assertThat(compare).isFalse();
    }

    public void willReturnMapOfLinks(){
//        assertEquals(3, crawler.crawl(dummyUrl)).size()) ;
    }




}
