# Simple crawler

## Introduction
The crawler in this application will take a url and essentially crawl pages within the dien domain.
It will return a site-map.json file in the local directory. The site-map json file returned is a simple map,
showing the urls in each page visited.

For instance given a url, www.example.com, the crawler would identify all the urls and static resources within the page, and then subsequently visiting each url as long as it is in the domain of the existing url.


## Design
The design takes into consideration the fact that a crawler could end up being a long running application because of the potential recursive nature due thanks to the depth of embedded urls, to this end,
this design sets a default crawl depth of 5 in he Crawler class. Additionally, users can optionally stipulate their preferred crawl depth, by appendiing an additional Integer argument. 

Given a url
- Ensure url is valid
- Specify crawl depth
- For each url return valid endpoints
+ return base urls
+ populate tree map
+ return tree map containing all valid endpoints
+ Serialize and print map