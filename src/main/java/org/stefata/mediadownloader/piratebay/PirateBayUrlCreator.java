package org.stefata.mediadownloader.piratebay;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class PirateBayUrlCreator {

    public String createSearchUrl(String pirateBayDomain, String searchCriteria) {
        var encodedSearch = URLEncoder.encode(searchCriteria, StandardCharsets.UTF_8);
        return "https://" +
                pirateBayDomain +
                "/search.php?q=" +
                encodedSearch;
    }


}
