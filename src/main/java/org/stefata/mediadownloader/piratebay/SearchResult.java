package org.stefata.mediadownloader.piratebay;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchResult {

    private final String title;
    private final String magnetLink;
    private final String urlPath;

}
