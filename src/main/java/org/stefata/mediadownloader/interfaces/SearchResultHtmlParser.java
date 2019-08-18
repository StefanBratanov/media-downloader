package org.stefata.mediadownloader.interfaces;

import org.jsoup.nodes.Document;
import org.stefata.mediadownloader.torrent.SearchResult;

import java.util.List;

public interface SearchResultHtmlParser {

    List<SearchResult> parse(Document document);

}
