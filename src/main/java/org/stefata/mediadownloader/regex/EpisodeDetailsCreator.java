package org.stefata.mediadownloader.regex;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.stefata.mediadownloader.shows.EpisodeDetails;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class EpisodeDetailsCreator {

    private static final List<Pattern> TITLE_PATTERNS = Lists.newLinkedList();

    static {
        TITLE_PATTERNS.add(Pattern.compile(".*S(?<season>\\d{1,3})E(?<episode>\\d{1,3}).*"));
    }

    public Optional<EpisodeDetails> fromTorrentTitle(String title) {
        return TITLE_PATTERNS.stream()
                .map(pattern -> pattern.matcher(title))
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> {
                    log.info("{} matched {}", title, matcher.pattern());
                    String season = matcher.group("season");
                    String episode = matcher.group("episode");
                    return EpisodeDetails.builder()
                            .season(Integer.parseInt(season))
                            .episode(Integer.parseInt(episode))
                            .build();
                });
    }
}
