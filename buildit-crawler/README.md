# Simple crawler

## Introduction
The crawler in this application will take a url and essentially crawl pages within the dien domain.
It will return a site-map.json file in the local directory. The site-map json file returned is a simple map,
showing the urls in each page visited.

For instance given a url, `www.example.com`, the crawler would identify all the urls and static resources within the page, and then subsequently visiting each url as long as it is in the domain of the existing url.
## Design
The design takes into consideration the fact that a crawler could end up being a long running application because of the potential recursive nature due thanks to the depth of embedded urls, to this end,
this design sets a default crawl depth of 5 in he Crawler class. Additionally, users can optionally stipulate their preferred crawl depth, by appendiing an additional Integer argument. 

## Running and Compiling
The solution to the problem is based on `Springboot` and it runs from a class called `com.buildit.crawler.BuilditCrawlerApplication`.
NOTE: You will need to have maven installed to run the application otherwise, I have included a maven wrapper.
On a linux or Mac computer, you can do while in the base dir of the project:
```
$ ./mvnw clean test
```
to compile and install the project. If you have maven already, you can do
```
$ mvn clean install
```
This will also compile and run all the tests.
On a Windows machine, do
```
$ ./mvnw.cmd clean install
```

After compiling, you can test with urls by entering:

```
mvn spring-boot:run -Dspring-boot.run.arguments="https://www.bbc.co.uk"
```
replacing `bbc.co.uk` with your own url to crawl. To crawl and specify your own crawl depth, do
```
mvn spring-boot:run -Dspring-boot.run.arguments="https://www.bbc.co.uk 2"
```
where you can replace `2` with your own crawl depth. 
Note that the crawl depth must not be greater than 5.

You can also run this using the mvn wrapper, see below:
```
$ ./mvnw spring-boot:run -Dspring-boot.run.arguments="https://www.bbc.co.uk"
```
or 
```
$ ./mvnw spring-boot:run -Dspring-boot.run.arguments="https://www.bbc.co.uk 2"
```

After running, look in your base directory for `site-map.json` for the site map.
You can use `ctrl+C` to stop the crawler at anytime.

## Testing
The application makes use of selected pages from [crawl test page](crawler-test.com), for tests.
I have also included some of the selected htmls under the directory `test_data`. The idea behind using this is simply based on the fact that this site 
provides enough scenarios for the crawling assignment. 

## Site-Map Structure
The structure of the sitemap is a simple json, whose first key is the url entered, and its value is a set of all urls found on the page.
The subsequent keys are the cralwed urls originating from the urls found on the root page, excluding urls outside the crawl url domain. Static content are also included.
e.g see sample below.
```$xslt
{
  "https://www.bbc.co.uk" : [ "https://www.bbc.co.uk", "https://www.bbc.co.uk#main-heading" ],
  "https://www.bbc.co.uk#main-heading" : [ "https://www.bbc.co.uk", "https://www.bbc.co.uk#main-heading", "https://www.bbc.co.uk/accessibility/" ],
  "https://www.bbc.co.uk/accessibility/" : [ "http://www.bbc.co.uk/" ],
  "http://www.bbc.co.uk/" : [ "https://www.bbc.co.uk", "https://www.bbc.co.uk/#main-heading" ],
  "https://www.bbc.co.uk/#main-heading" : [ "https://www.bbc.co.uk", "https://www.bbc.co.uk/#main-heading", "https://www.bbc.co.uk/accessibility/", "https://account.bbc.com/account?lang=en-GB&ptrt=https://www.bbc.co.uk/", "https://www.bbc.co.uk/notifications" ],
  "https://www.bbc.co.uk/notifications" : [ "http://www.bbc.co.uk" ]
}
```
## Potential Improvements
Potential improvements will be:
- Make the application run faster by potentially using more threads. So this will involve having a thread pool with additional threads running in parallel processing urls and populating the index.
- From a design standpoint, move some of the default parameters like the default crawl depth into a properties file so that it is easily configurable
- Using a database (Mongo or even Couchbase) as a sort of index for the crawled pages so that the can be persisted there and also to enhance speedy retrieval of Key Value pairs
- Perhaps use a different library other than `Jsoup`. One would be `Crawler4j` which is more configurable and supports configurations like setting the number of threads, run time of the crawler and crawl depth.
- Reacrchitecting the code so that it is reusable and applicable to other crawl domains besides websites. For instance local directories etc
