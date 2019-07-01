package org.stefata.mediadownloader.html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.stefata.mediadownloader.piratebay.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchResultHtmlParser {

    public List<SearchResult> parse(Document document) {
        return document
                .select("table#searchResult")
                .select("tr")
                .stream()
                .filter(elem -> !elem.hasClass("header"))
                .flatMap(elem -> elem.select("td").stream())
                .filter(elem -> !elem.hasClass("vertTh"))
                .filter(elem -> !elem.hasAttr("align"))
                .map(elem -> {
                    Element detLink = elem.selectFirst(".detLink");
                    String magnetLink = elem.select("a[title^=Download]")
                            .attr("href");
                    return SearchResult.builder()
                            .title(detLink.html())
                            .urlPath(detLink.attr("href"))
                            .magnetLink(magnetLink)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
