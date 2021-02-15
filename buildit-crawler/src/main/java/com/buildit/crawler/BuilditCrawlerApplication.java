package com.buildit.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuilditCrawlerApplication {

	private static String INVALID_ARG_MESSAGE = "You've entered an invalid number of arguments, make sure, argument1 is a string url and arg2 (crawl depth) which is optional is 0 or a positive integer that is less than 5";
	private static final Logger LOGGER = LoggerFactory.getLogger(BuilditCrawlerApplication.class);

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		int argLength = args.length;
		if(argLength == 0 || argLength > 2) {
			LOGGER.info(INVALID_ARG_MESSAGE);
		}
		else if (argLength == 1){
			LOGGER.info("PREPARING TO START CRAWLING");
			crawler.crawl(args[0]);
		}
		else if (argLength == 2){
			LOGGER.info("PREPARING TO START CRAWLING");
			crawler.crawl(args[0], Integer.parseInt(args[2]));
		}

		SpringApplication.run(BuilditCrawlerApplication.class, args);
	}

}
