package org.stefata.mediadownloader.flow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.stefata.mediadownloader.html.HtmlExtractor;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.model.TvShowControlTable;
import org.stefata.mediadownloader.persistence.repository.TvShowControlTableRepository;
import org.stefata.mediadownloader.piratebay.PirateBayUrlCreator;
import org.stefata.mediadownloader.piratebay.Proxy;
import org.stefata.mediadownloader.piratebay.ProxySearch;
import org.stefata.mediadownloader.html.SearchResultHtmlParser;
import org.stefata.mediadownloader.regex.EpisodeDetailsCreator;
import org.stefata.mediadownloader.shows.EpisodeDetails;
import org.stefata.mediadownloader.torrent.TorrentDownloader;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TvShowFlow {

    private final TvShowControlTableRepository tvShowControlTableRepository;
    private final ProxySearch proxySearch;
    private final PirateBayUrlCreator pirateBayUrlCreator;
    private final HtmlExtractor htmlExtractor;
    private final SearchResultHtmlParser searchResultHtmlParser;
    private final EpisodeDetailsCreator episodeDetailsCreator;
    private final TorrentDownloader torrentDownloader;

    public void runFlow(TvShow tvShow) {
        String tvShowTitle = tvShow.getTitle();
        log.info("Running flow for {}", tvShowTitle);
        Optional<TvShowControlTable> maybeControlTable =
                tvShowControlTableRepository.findByTvShow_Title(tvShowTitle);
        if (maybeControlTable.isEmpty()) {
            log.info("Control Table does not exist for {}. Will skip searching",
                    tvShowTitle);
            return;
        }
        TvShowControlTable controlTable = maybeControlTable.get();
        proxySearch.getProxy()
                .map(Proxy::getDomain)
                .map(domain -> pirateBayUrlCreator.createSearchUrl(domain, tvShowTitle))
                .map(htmlExtractor::fromUrl)
                .map(searchResultHtmlParser::parse)
                .flatMapMany(Flux::fromIterable)
                .filter(searchResult -> {
                    String torrentTitle = searchResult.getTitle();
                    Optional<EpisodeDetails> maybeEpisodeDetails =
                            episodeDetailsCreator.fromTorrentTitle(torrentTitle);
                    if (maybeEpisodeDetails.isEmpty()) {
                        return false;
                    }
                    EpisodeDetails episodeDetails = maybeEpisodeDetails.get();
                    if (episodeHasNotBeenDownloaded(controlTable, episodeDetails)) {
                        log.info("{} will be downloaded", torrentTitle);
                        return true;
                    }
                    return false;
                })
                .next()
                .subscribe(torrentDownloader::download);
    }

    private boolean episodeHasNotBeenDownloaded(TvShowControlTable controlTable,
                                                EpisodeDetails episodeDetails) {
        return episodeDetails.getSeason() >= controlTable.getCurrentSeason() &&
                episodeDetails.getEpisode() > controlTable.getLastDownloadedEpisode();
    }
}
