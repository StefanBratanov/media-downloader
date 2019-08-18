package org.stefata.mediadownloader.limetorrents;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.torrent.SearchResult;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.stefata.mediadownloader.TestResources.readResource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LimeTorrentsSearchResultParser.class)
class LimeTorrentsSearchResultParserTest {

    @Autowired
    private LimeTorrentsSearchResultParser subject;

    @Test
    public void parsesSearchResultHtml() throws URISyntaxException, MalformedURLException {
        String searchPageSource = readResource("/search-results/limetorrents.html");
        Document document = Jsoup.parse(searchPageSource);

        List<SearchResult> result = subject.parse(document);

        SearchResult expectedFirst = SearchResult.builder()
                .title("Stranger Things S03 LostFilm")
                .torrentUrl(new URL("http://itorrents.org/torrent/57B09AF181EFD26D7705675D5A90B162E28A11A8.torrent?" +
                        "title=Stranger-Things-S03-LostFilm"))
                .build();

        assertThat(result).hasSize(40);
        assertThat(result).first().isEqualTo(expectedFirst);
        assertThat(result).allSatisfy(searchResult -> {
            assertThat(searchResult.getTitle()).isNotNull();
            assertThat(searchResult.getTorrentUrl()).isNotNull();
        });
    }

}