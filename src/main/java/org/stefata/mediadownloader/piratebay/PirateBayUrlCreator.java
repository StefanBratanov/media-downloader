package org.stefata.mediadownloader.piratebay;

import org.stefata.mediadownloader.interfaces.UrlCreator;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PirateBayUrlCreator implements UrlCreator  {

    public String createSearchUrl(String domain, String searchCriteria) {
        var encodedSearch = URLEncoder.encode(searchCriteria, StandardCharsets.UTF_8);
        return "https://" +
                domain +
                "/search.php?q=" +
                encodedSearch;
    }


}
