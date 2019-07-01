package org.stefata.mediadownloader.flow;

import org.awaitility.Duration;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.html.HtmlExtractor;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.model.TvShowControlTable;
import org.stefata.mediadownloader.persistence.repository.TvShowControlTableRepository;
import org.stefata.mediadownloader.piratebay.*;
import org.stefata.mediadownloader.regex.EpisodeDetailsCreator;
import org.stefata.mediadownloader.shows.EpisodeDetails;
import org.stefata.mediadownloader.torrent.TorrentDownloader;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TvShowFlow.class)
class TvShowFlowTest {

    private static final String TITLE = "Pine Gap";
    private static final TvShow TV_SHOW = TvShow.builder()
            .title(TITLE)
            .build();
    private static final TvShowControlTable TV_SHOW_CONTROL_TABLE = TvShowControlTable.builder()
            .tvShow(TV_SHOW)
            .currentSeason(1)
            .lastDownloadedEpisode(5)
            .build();
    private static final SearchResult VALID_SEARCH_RESULT = SearchResult.builder()
            .title("Pine Gap something")
            .build();

    @Mock
    private Document document;

    @MockBean
    private TvShowControlTableRepository tvShowControlTableRepository;
    @MockBean
    private ProxySearch proxySearch;
    @MockBean
    private PirateBayUrlCreator pirateBayUrlCreator;
    @MockBean
    private HtmlExtractor htmlExtractor;
    @MockBean
    private SearchResultHtmlParser searchResultHtmlParser;
    @MockBean
    private EpisodeDetailsCreator episodeDetailsCreator;
    @MockBean
    private TorrentDownloader torrentDownloader;

    @Autowired
    private TvShowFlow subject;

    @Test
    public void doesNotRunFlowIfNoControlTableEntry() {
        when(tvShowControlTableRepository.findByTvShow_Title(TITLE))
                .thenReturn(Optional.empty());
        subject.runFlow(TV_SHOW);
        verifyZeroInteractions(proxySearch);
    }

    @Test
    public void testRunningFlow() {
        when(tvShowControlTableRepository.findByTvShow_Title(TITLE))
                .thenReturn(Optional.of(TV_SHOW_CONTROL_TABLE));

        Proxy proxy = Proxy.builder().domain("xxx.com").build();

        when(proxySearch.getProxy()).thenReturn(Mono.just(proxy));
        when(pirateBayUrlCreator.createSearchUrl("xxx.com", TITLE))
                .thenReturn("https://xxx.com/lolly");

        when(htmlExtractor.fromUrl("https://xxx.com/lolly")).thenReturn(document);

        SearchResult searchResult1 = SearchResult.builder()
                .title("title1")
                .build();

        SearchResult searchResult2 = SearchResult.builder()
                .title("title2")
                .build();

        SearchResult searchResult3 = SearchResult.builder()
                .title("title3")
                .build();

        when(searchResultHtmlParser.parse(document)).thenReturn(Arrays.asList(searchResult1,
                searchResult2, searchResult3, VALID_SEARCH_RESULT));

        //empty case
        when(episodeDetailsCreator.fromTorrentTitle("title1")).thenReturn(Optional.empty());
        //previous episode
        when(episodeDetailsCreator.fromTorrentTitle("title2")).thenReturn(Optional.of(
                EpisodeDetails.builder()
                        .season(1)
                        .episode(4)
                        .build()
        ));
        //current episode
        when(episodeDetailsCreator.fromTorrentTitle("title3")).thenReturn(Optional.of(
                EpisodeDetails.builder()
                        .season(1)
                        .episode(5)
                        .build()
        ));
        //not downloaded (valid scenario)
        when(episodeDetailsCreator.fromTorrentTitle("Pine Gap something")).thenReturn(Optional.of(
                EpisodeDetails.builder()
                        .season(1)
                        .episode(6)
                        .build()
        ));

        //running flow
        subject.runFlow(TV_SHOW);

        await()
                .atMost(Duration.FIVE_SECONDS)
                .untilAsserted(() -> {
                    verify(torrentDownloader).download(VALID_SEARCH_RESULT);
                });

        verifyNoMoreInteractions(torrentDownloader);

    }


}