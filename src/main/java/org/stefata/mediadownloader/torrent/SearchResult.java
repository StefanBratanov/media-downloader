package org.stefata.mediadownloader.torrent;

import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Builder(toBuilder = true)
@Data
public class SearchResult {

    private final String title;
    private final String magnetLink;
    private final URL torrentUrl;

}
