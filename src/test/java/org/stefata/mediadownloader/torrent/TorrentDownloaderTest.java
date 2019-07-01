package org.stefata.mediadownloader.torrent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.piratebay.SearchResult;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TorrentDownloader.class)
class TorrentDownloaderTest {

    @Autowired
    private TorrentDownloader subject;

    @Test
    public void testDownloading() {
        subject.download(SearchResult.builder().build());
    }

}