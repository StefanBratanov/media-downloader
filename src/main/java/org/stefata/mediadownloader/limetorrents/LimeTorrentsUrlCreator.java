package org.stefata.mediadownloader.limetorrents;

import org.springframework.stereotype.Component;
import org.stefata.mediadownloader.interfaces.UrlCreator;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class LimeTorrentsUrlCreator implements UrlCreator {

    @Override
    public String createSearchUrl(String domain, String searchCriteria) {
        var encodedSearch = URLEncoder.encode(searchCriteria, StandardCharsets.UTF_8);
        return domain + "/search/all/" + encodedSearch;
    }
}
