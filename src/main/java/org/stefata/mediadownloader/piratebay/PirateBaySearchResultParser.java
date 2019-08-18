package org.stefata.mediadownloader.piratebay;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.stefata.mediadownloader.interfaces.SearchResultHtmlParser;
import org.stefata.mediadownloader.torrent.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

public class PirateBaySearchResultParser implements SearchResultHtmlParser {

    @Override
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
                            .magnetLink(magnetLink)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
