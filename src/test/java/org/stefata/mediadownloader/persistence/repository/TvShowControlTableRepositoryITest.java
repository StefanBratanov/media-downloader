package org.stefata.mediadownloader.persistence.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.DatabaseIT;
import org.stefata.mediadownloader.MediaDownloaderApp;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.model.TvShowControlTable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MediaDownloaderApp.class)
class TvShowControlTableRepositoryITest extends DatabaseIT {

    @Test
    public void savesTvShowControlTable() {
        TvShow tvShow = TvShow.builder()
                .title("Pine Gap")
                .build();

        TvShow anotherTvShow = TvShow.builder()
                .title("Doctor Who")
                .build();

        tvShowRepository.save(tvShow);

        TvShowControlTable tvShowControlTable = TvShowControlTable.builder()
                .tvShow(tvShow)
                .currentSeason(1)
                .lastDownloadedEpisode(5)
                .build();

        TvShowControlTable result = tvShowControlTableRepository.save(tvShowControlTable);

        assertThat(result).isEqualTo(tvShowControlTable);
        assertThat(result.getId()).isNotNull().isGreaterThan(0L);

        assertThat(result.getTvShow()).isEqualTo(tvShow);

        TvShowControlTable invalidTvShowControlTable = TvShowControlTable.builder()
                .tvShow(anotherTvShow)
                .currentSeason(1)
                .lastDownloadedEpisode(6)
                .build();

        Exception exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () ->
                tvShowControlTableRepository.save(invalidTvShowControlTable));

        assertThat(exception)
                .hasStackTraceContaining("org.hibernate.TransientPropertyValueException: " +
                        "object references an unsaved transient instance");

    }

    @Test
    public void retrievesControlTableByTitle() {
        TvShow tvShow = TvShow.builder()
                .title("Pine Gap")
                .build();

        tvShowRepository.save(tvShow);

        TvShowControlTable tvShowControlTable = TvShowControlTable.builder()
                .tvShow(tvShow)
                .currentSeason(1)
                .lastDownloadedEpisode(5)
                .build();

        tvShowControlTableRepository.save(tvShowControlTable);

        Optional<TvShowControlTable> result = tvShowControlTableRepository
                .findByTvShow_Title("Pine Gap");

        assertThat(result).hasValue(tvShowControlTable);

        Optional<TvShowControlTable> invalid = tvShowControlTableRepository
                .findByTvShow_Title("Game of Thrones");

        assertThat(invalid).isEmpty();

        assertThat(tvShowControlTableRepository
                .findByTvShow_Title("pine gap")).isEmpty();
    }

}