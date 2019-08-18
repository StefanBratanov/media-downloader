package org.stefata.mediadownloader.torrent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class TorrentDownloader {

    public CompletableFuture<?> download(URL torrentUrl) {
        log.info("Downloading torrent from {}", torrentUrl);
        return CompletableFuture.completedFuture(new Object());
    }

    public CompletableFuture<?> download(String magnetLink) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
