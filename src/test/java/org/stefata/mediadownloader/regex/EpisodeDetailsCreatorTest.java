package org.stefata.mediadownloader.regex;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.shows.EpisodeDetails;

import java.util.Optional;

import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EpisodeDetailsCreator.class)
class EpisodeDetailsCreatorTest {

    @Autowired
    private EpisodeDetailsCreator subject;

    @ParameterizedTest
    @CsvSource({
            "Pine.Gap.S01E05.1080p.HDTV.h264-CCT,1,5",
            "Pine.Gap.S01.SweSub+MultiSubs.1080p.x264-Justiso,,",
            "Pine.Gap.S01E03.HDTV.x264-W4F,1,3",
            "Pine.Gap.S01E01E02.1080p.HDTV.H264-CBFM,1,1",
            "Pine.Gap.S01E03.WEBRip.x264-ION10,1,3",
            "Stranger.Things.S02E07.iNTERNAL.WEB.x264-STRiFE,2,7",
            "Game.of.Thrones.S08E05.720p.WEB.H264-MEMENTO[ettv],8,5",
            "The.Expanse.S03E09.720p.HDTV.x264-LucidTV[ettv],3,9"

    })
    public void createsDetailsFromTorrentTitle(String title, Integer season, Integer episode) {
        Optional<EpisodeDetails> maybeResult = subject.fromTorrentTitle(title);

        if (isNull(season) && isNull(episode)) {
            assertThat(maybeResult).isEmpty();
            return;
        }

        EpisodeDetails expectedEpisodeDetails = EpisodeDetails.builder()
                .season(season)
                .episode(episode)
                .build();

        assertThat(maybeResult).hasValue(expectedEpisodeDetails);
    }

}