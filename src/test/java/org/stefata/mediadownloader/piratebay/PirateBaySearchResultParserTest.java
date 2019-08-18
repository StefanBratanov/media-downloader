package org.stefata.mediadownloader.piratebay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.TestResources;
import org.stefata.mediadownloader.torrent.SearchResult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.stefata.mediadownloader.TestResources.readResource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PirateBaySearchResultParser.class)
class PirateBaySearchResultParserTest {

    @Autowired
    private PirateBaySearchResultParser subject;

    @Test
    public void parsesSearchResultHtml() throws URISyntaxException {
        String searchPageSource = readResource("/search-results/piratebay.html");
        Document document = Jsoup.parse(searchPageSource);

        List<SearchResult> result = subject.parse(document);

        SearchResult expectedFirst = SearchResult.builder()
                .title("Stranger Things Season 1 720p WebRip EN-SUB x264-[MULVAcoded]")
                .magnetLink("magnet:?xt=urn:btih:db47f718eba63a8fb431558ad00ef8b76e4698" +
                        "a0&dn=Stranger+Things+Season+1+720p+WebRip+EN-SUB+x264-%5BMULVAcode" +
                        "d%5D&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F" +
                        "%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A13" +
                        "37&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus." +
                        "desync.com%3A6969")
                .build();

        assertThat(result).hasSize(30);
        assertThat(result).first().isEqualTo(expectedFirst);
        assertThat(result).allMatch(Objects::nonNull);

    }

}