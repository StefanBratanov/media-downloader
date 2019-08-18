package org.stefata.mediadownloader.limetorrents;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.stefata.mediadownloader.interfaces.SearchResultHtmlParser;
import org.stefata.mediadownloader.torrent.SearchResult;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static pl.touk.throwing.ThrowingSupplier.unchecked;

@Component
public class LimeTorrentsSearchResultParser implements SearchResultHtmlParser {

    @Override
    public List<SearchResult> parse(Document document) {
        return document
                .select("table.table2")
                .select("tr")
                .stream()
                .filter(elem -> elem.hasAttr("bgcolor"))
                .map(elem -> {
                    Element ttName = elem.selectFirst("div.tt-name");
                    String torrentLink = ttName.selectFirst("a")
                            .attr("href");
                    String title = ttName.select("a:not([rel])").html();
                    URL torrentUrl = unchecked(() -> new URL(torrentLink)).get();
                    return SearchResult.builder()
                            .title(title)
                            .torrentUrl(torrentUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
