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

    String testURL1 = "http://www.bbc.co.uk";
    String root = "https://crawler-test.com/";
    String linksWithFileTypes = "https://crawler-test.com/urls/links_to_non_html_filetypes";
    String repeatedNonFollowExternalLinks = "https://crawler-test.com/links/page_with_external_links";

    @BeforeEach
    void init(){
        crawler = new Crawler();
    }

    @Test
    @DisplayName("Invalid depth exception")
    public void willReturnExceptionForInvalidDepth() {
        assertThrows(InvalidDepthException.class, () -> {
            crawler.crawl(testURL1, -1);
        });
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

    @Test
    public void willReturnExceptionForInvalidOrNullURL() {
        assertThrows(InvalidCrawlURLException.class, () -> {
            crawler.crawl("invalidURL", 1);
        });
    }

}
