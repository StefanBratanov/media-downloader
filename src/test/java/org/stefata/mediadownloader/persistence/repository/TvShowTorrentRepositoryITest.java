package org.stefata.mediadownloader.persistence.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.DatabaseIT;
import org.stefata.mediadownloader.MediaDownloaderApp;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.model.TvShowTorrent;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MediaDownloaderApp.class)
class TvShowTorrentRepositoryITest extends DatabaseIT {

    @Test
    public void savesTvShowTorrent() throws MalformedURLException {
        TvShow tvShow = TvShow.builder().title("Stranger things").build();

        tvShowRepository.save(tvShow);

        TvShowTorrent tvShowTorrent = TvShowTorrent.builder()
                .tvShow(tvShow)
                .episode(4)
                .season(8)
                .torrentTitle("Game of thrones S04E08")
                .torrentUrl(new URL("https://google.com"))
                .build();

        TvShowTorrent result = tvShowTorrentRepository.save(tvShowTorrent);

        assertThat(result).isEqualTo(tvShowTorrent);
        assertThat(tvShowTorrentRepository.findById(result.getId()))
                .hasValue(result);

    }

}