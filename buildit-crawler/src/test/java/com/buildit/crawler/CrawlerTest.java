package com.buildit.crawler;

import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.buildit.crawler.exceptions.InvalidDepthException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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




}
