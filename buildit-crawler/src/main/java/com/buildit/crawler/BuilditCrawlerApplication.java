package com.buildit.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuilditCrawlerApplication {

	public static void main(String[] args) {
		if (args.length > 2){
			System.out.println("You've entered an invalid number of arguments, make sure, arg1 = url string and arg2 which is optional is 0 or a positive integer");
		}
		SpringApplication.run(BuilditCrawlerApplication.class, args);
	}

}
