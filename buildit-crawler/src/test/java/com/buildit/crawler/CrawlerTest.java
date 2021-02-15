package com.buildit.crawler;

import com.buildit.crawler.exceptions.InvalidCrawlURLException;
import com.buildit.crawler.exceptions.InvalidDepthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrawlerTest {

    Crawler crawler;
    String root = "https://crawler-test.com/";
    String linksWithFileTypes = "https://crawler-test.com/urls/links_to_non_html_filetypes";
    String repeatedNonFollowExternalLinks = "https://crawler-test.com/links/page_with_external_links";
    String externalLinks = "https://crawler-test.com/links/external_links_to_disallwed_urls";


    @BeforeEach
    void init(){
        crawler = new Crawler();
    }

    @Test
    @DisplayName("Invalid depth exception")
    public void willReturnExceptionForInvalidDepth() {
        assertThrows(InvalidDepthException.class, () -> {
            crawler.crawl(root, -1);
        });
    }

    @Test
    @DisplayName("Invalid depth exception")
    public void willReturnExceptionForInvalidDepthWhenDepthIsGreaterThanDefaultDepth() {
        assertThrows(InvalidDepthException.class, () -> {
            crawler.crawl(root, 6);
        });
    }


    @Test
    @DisplayName("Run default crawler")
    public void willRunDefaultCrawlOnNonFollowExternalLinks() {
        //when
        Map<String, Set> indexOfCrawledLinks = crawler.crawl(repeatedNonFollowExternalLinks);
        List<String> staticContent = Arrays.asList("https://crawler-test.com/", "http://robotto.org", "http://semetrical.com", "http://deepcrawl.co.uk");
        Set<String> indexedElements = indexOfCrawledLinks.get(repeatedNonFollowExternalLinks);

        //then
        assertThat(indexedElements.size()).isEqualTo(4);
        assertThat(root).isEqualTo(indexedElements.iterator().next());
        assertThat(indexedElements.containsAll(staticContent));
    }

    @Test
    @DisplayName("Count of static endpoints returned")
    public void willReturnStaticContent() {
        //when
        Map<String, Set> indexOfCrawledLinks = crawler.crawl(linksWithFileTypes, 0);
        List<String> staticContent = Arrays.asList("https://crawler-test.com/", "https://crawler-test.com/images/logo_small.jpg", "https://crawler-test.com/images/logo_small.JPG", "https://crawler-test.com/pdf_open_parameters.pdf", "https://crawler-test.com/pdf_open_parameters.PDF", "https://crawler-test.com/Dashboard/Charts/FCF_Column2D.swf", "https://crawler-test.com/Dashboard/Charts/FCF_Column2D.SWF");

        //then
        Set<String> indexedElements = indexOfCrawledLinks.get("https://crawler-test.com/urls/links_to_non_html_filetypes");
        assertThat(indexedElements.size()).isEqualTo(7);
        assertThat(indexedElements.containsAll(staticContent));
    }

    @Test
    @DisplayName("sitemap for repeated external links")
    public void willReturnSiteMapWith3ExternalLinksAnd1RootLinkWhenPageHas7RepeatedLinks() {
        //when
        Map<String, Set> indexOfCrawledLinks = crawler.crawl(repeatedNonFollowExternalLinks, 0);
        List<String> staticContent = Arrays.asList("https://crawler-test.com/", "http://robotto.org", "http://semetrical.com", "http://deepcrawl.co.uk");
        Set<String> indexedElements = indexOfCrawledLinks.get(repeatedNonFollowExternalLinks);

        //then
        assertThat(indexedElements.size()).isEqualTo(4);
        assertThat(root).isEqualTo(indexedElements.iterator().next());
        assertThat(indexedElements.containsAll(staticContent));
    }

    /**
     * This page consists of 6 links
     * 4 external links to dailymail, the guardian, the sun and the independent
     * This test will return all 4 external links as elements of the main link but will not
     * visit those pages.
     * It will visit the link to the root page and return links associated with it
     */
    @Test
    @DisplayName("sitemap for repeated external links")
    public void willReturnAllExternalLinksWithoutCrwalingThem() {
        //consists of 6 links
        //when
        Map<String, Set> indexOfExternalLinks = crawler.crawl(externalLinks, 0);
        List<String> externalLinks = Arrays.asList("http://www.dailymail.co.uk/money/index.html", "http://www.independent.co.uk/independentplus/", "http://www.theguardian.com/whsmiths/", "http://www.thesun.co.uk/sol/homepage/showbiz/");
        Set<String> indexedElements = indexOfExternalLinks.get(repeatedNonFollowExternalLinks);

        //then -- assert that the site map does not have any of the external elments as a key
        assertThat(indexOfExternalLinks.containsKey(externalLinks.get(0))).isFalse();
        assertThat(indexOfExternalLinks.containsKey(externalLinks.get(1))).isFalse();
        assertThat(indexOfExternalLinks.containsKey(externalLinks.get(2))).isFalse();
        assertThat(indexOfExternalLinks.containsKey(externalLinks.get(3))).isFalse();

    }


    @Test
    public void willReturnExceptionForInvalidOrNullURL() {
        assertThrows(InvalidCrawlURLException.class, () -> {
            crawler.crawl("invalidURL", 1);
        });
    }

}
