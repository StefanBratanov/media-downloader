package org.stefata.mediadownloader.shows;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EpisodeDetails {

    private final Integer season;
    private final Integer episode;

}
