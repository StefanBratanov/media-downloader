package org.stefata.mediadownloader.persistence.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.DatabaseIT;
import org.stefata.mediadownloader.MediaDownloaderApp;
import org.stefata.mediadownloader.persistence.model.TvShow;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MediaDownloaderApp.class)
class TvShowRepositoryITest extends DatabaseIT {

    @Autowired
    private TvShowRepository subject;

    @Test
    public void savesTvShow() {
        TvShow tvShow = TvShow.builder()
                .title("Game of thrones")
                .build();

        TvShow result = subject.save(tvShow);

        assertThat(result).isEqualTo(tvShow);
        assertThat(result.getId()).isNotNull().isGreaterThan(0L);
    }

}