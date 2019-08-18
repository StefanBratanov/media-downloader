package org.stefata.mediadownloader.flow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
import org.stefata.mediadownloader.torrent.TorrentDownloader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.net.URL;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class TvShowFlow {

    private final TvShowControlTableRepository tvShowControlTableRepository;

    @Value("${torrent.site.domain}")
    private String torrentSiteDomain;

    private final UrlCreator urlCreator;
    private final HtmlExtractor htmlExtractor;
    private final SearchResultHtmlParser searchResultHtmlParser;
    private final EpisodeDetailsCreator episodeDetailsCreator;
    private final TorrentDownloader torrentDownloader;
    private final TvShowTorrentRepository tvShowTorrentRepository;

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
        Mono.just(urlCreator.createSearchUrl(torrentSiteDomain,tvShowTitle))
                .map(htmlExtractor::fromUrl)
                .map(searchResultHtmlParser::parse)
                .flatMapMany(Flux::fromIterable)
                .map(searchResult -> {
                    String torrentTitle = searchResult.getTitle();
                    Optional<EpisodeDetails> maybeEpisodeDetails =
                            episodeDetailsCreator.fromTorrentTitle(torrentTitle);
                    if (maybeEpisodeDetails.isEmpty()) {
                        return TvShowTorrent.builder().build();
                    }
                    EpisodeDetails episodeDetails = maybeEpisodeDetails.get();
                    if (episodeHasNotBeenDownloaded(controlTable, episodeDetails)) {
                        log.info("Will download Season {}, Episode {} for {}",
                                episodeDetails.getSeason(),
                                episodeDetails.getEpisode(), tvShowTitle);
                        return TvShowTorrent.builder()
                                .tvShow(tvShow)
                                .torrentTitle(torrentTitle)
                                .season(episodeDetails.getSeason())
                                .episode(episodeDetails.getEpisode())
                                .torrentUrl(searchResult.getTorrentUrl())
                                .build();
                    }
                    return TvShowTorrent.builder().build();
                })
                .filter(tvShowTorrent -> nonNull(tvShowTorrent.getTvShow()))
                .next()
                .zipWhen(tvShowTorrent -> {
                    URL torrentUrl = tvShowTorrent.getTorrentUrl();
                    log.info("Starting to download {} from {}",
                            tvShowTorrent.getTorrentTitle(), torrentUrl);
                    return Mono.fromFuture(torrentDownloader.download(torrentUrl));
                })
                .map(Tuple2::getT1)
                .subscribe(tvShowTorrent -> {
                    log.info("Successfully downloaded {}", tvShowTorrent.getTorrentTitle());
                    tvShowTorrentRepository.save(tvShowTorrent);
                    log.info("Updating control table for {}", tvShowTitle);
                    controlTable.setCurrentSeason(tvShowTorrent.getSeason());
                    controlTable.setLastDownloadedEpisode(tvShowTorrent.getEpisode());
                    tvShowControlTableRepository.save(controlTable);
                });
    }

    private boolean episodeHasNotBeenDownloaded(TvShowControlTable controlTable,
                                                EpisodeDetails episodeDetails) {
        return episodeDetails.getSeason() >= controlTable.getCurrentSeason() &&
                episodeDetails.getEpisode() > controlTable.getLastDownloadedEpisode();
    }
}
