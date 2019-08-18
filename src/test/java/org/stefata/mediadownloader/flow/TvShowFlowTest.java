package org.stefata.mediadownloader.flow;

import org.awaitility.Duration;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.html.HtmlExtractor;
import org.stefata.mediadownloader.interfaces.SearchResultHtmlParser;
import org.stefata.mediadownloader.interfaces.UrlCreator;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.model.TvShowControlTable;
import org.stefata.mediadownloader.persistence.model.TvShowTorrent;
import org.stefata.mediadownloader.persistence.repository.TvShowControlTableRepository;
import org.stefata.mediadownloader.persistence.repository.TvShowTorrentRepository;
import org.stefata.mediadownloader.regex.EpisodeDetailsCreator;
import org.stefata.mediadownloader.shows.EpisodeDetails;
import org.stefata.mediadownloader.torrent.SearchResult;
import org.stefata.mediadownloader.torrent.TorrentDownloader;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;
import static pl.touk.throwing.ThrowingSupplier.unchecked;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TvShowFlow.class)
@ActiveProfiles("test")
class TvShowFlowTest {

    private static final URL TORRENT_URL = unchecked(() ->
            new URL("https://www.google.com")).get();

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
            .magnetLink("magnet?:xxx")
            .torrentUrl(TORRENT_URL)
            .build();

    @Mock
    private Document document;

    @MockBean
    private TvShowControlTableRepository tvShowControlTableRepository;
    @MockBean
    private UrlCreator urlCreator;
    @MockBean
    private HtmlExtractor htmlExtractor;
    @MockBean
    private SearchResultHtmlParser searchResultHtmlParser;
    @MockBean
    private EpisodeDetailsCreator episodeDetailsCreator;
    @MockBean
    private TorrentDownloader torrentDownloader;
    @MockBean
    private TvShowTorrentRepository tvShowTorrentRepository;

    @Captor
    private ArgumentCaptor<TvShowControlTable> argumentCaptor;

    @Autowired
    private TvShowFlow subject;

    @Test
    public void doesNotRunFlowIfNoControlTableEntry() {
        when(tvShowControlTableRepository.findByTvShow_Title(TITLE))
                .thenReturn(Optional.empty());
        subject.runFlow(TV_SHOW);
        verifyZeroInteractions(urlCreator);
    }

    @Test
    public void testRunningFlow() {
        when(tvShowControlTableRepository.findByTvShow_Title(TITLE))
                .thenReturn(Optional.of(TV_SHOW_CONTROL_TABLE));

        when(urlCreator.createSearchUrl("xxx.com", TITLE))
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

        CompletableFuture future = CompletableFuture.completedFuture(new Object());
        when(torrentDownloader.download(TORRENT_URL)).thenReturn(future);

        //running flow
        subject.runFlow(TV_SHOW);

        TvShowTorrent expectedTvShowTorrent = TvShowTorrent.builder()
                .tvShow(TV_SHOW)
                .torrentTitle("Pine Gap something")
                .season(1)
                .episode(6)
                .torrentUrl(TORRENT_URL)
                .build();

        await()
                .atMost(Duration.TEN_SECONDS)
                .untilAsserted(() -> {
                    verify(torrentDownloader).download(TORRENT_URL);
                    verify(tvShowTorrentRepository).save(expectedTvShowTorrent);
                    verify(tvShowControlTableRepository).save(argumentCaptor.capture());
                    TvShowControlTable controlTable = argumentCaptor.getValue();
                    assertThat(controlTable.getCurrentSeason()).isEqualTo(1);
                    assertThat(controlTable.getLastDownloadedEpisode()).isEqualTo(6);
                });

        verifyNoMoreInteractions(torrentDownloader);
        verifyNoMoreInteractions(tvShowTorrentRepository);

    }


}