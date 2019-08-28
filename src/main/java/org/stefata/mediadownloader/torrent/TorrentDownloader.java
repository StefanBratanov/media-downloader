package org.stefata.mediadownloader.torrent;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.touk.throwing.ThrowingRunnable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static org.apache.commons.io.FileUtils.copyURLToFile;
import static pl.touk.throwing.ThrowingSupplier.unchecked;

@Slf4j
@Component
@RequiredArgsConstructor
public class TorrentDownloader {

    private static InetAddress LOCAL_HOST_ADDRESS =
            unchecked(InetAddress::getLocalHost).get();

    @Value("${torrent.download.path}")
    private Path torrentDownloadPath;

    public CompletableFuture<?> download(URL torrentUrl) {
        log.info("Downloading torrent from {}", torrentUrl);
        var url = unchecked(() -> getRedirectUrlIfRequired(torrentUrl))
                .get();
        var torrentFilename = FilenameUtils.getName(url.getPath());
        Path torrentPath = torrentDownloadPath.resolve(torrentFilename);
        ThrowingRunnable.unchecked(() -> copyURLToFile(url,
                torrentPath.toFile())).run();
        return CompletableFuture
                .supplyAsync(() -> unchecked(() -> {
                    SharedTorrent torrent = SharedTorrent.fromFile(
                            torrentPath.toFile(),
                            torrentDownloadPath.toFile());
                    return new Client(LOCAL_HOST_ADDRESS, torrent);
                }).get())
                .thenAccept(client -> {
                    client.download();
                    client.waitForCompletion();
                    client.stop();
                });
    }

    public CompletableFuture<?> download(String magnetLink) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private URL getRedirectUrlIfRequired(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
            return new URL(connection.getHeaderField("Location"));
        }
        return url;
    }
}
