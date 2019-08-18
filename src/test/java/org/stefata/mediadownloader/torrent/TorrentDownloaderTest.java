package org.stefata.mediadownloader.torrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TorrentDownloader.class)
class TorrentDownloaderTest {

    @Autowired
    private TorrentDownloader subject;

    @Test
    public void testDownloading() {
        //TODO: create test
    }

    @Test
    public void testDownloadingUsingMagnetLink() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                subject.download("magnet:?xt=urn"));
    }

}