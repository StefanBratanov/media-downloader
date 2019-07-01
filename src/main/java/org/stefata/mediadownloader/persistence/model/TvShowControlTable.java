package org.stefata.mediadownloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TV_SHOWS_CONTROL_TABLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TvShowControlTable implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "TITLE",referencedColumnName = "TITLE")
    private TvShow tvShow;

    @Column(name = "CURRENT_SEASON")
    private Integer currentSeason;

    @Column(name = "LAST_DOWNLOADED_EPISODE")
    private Integer lastDownloadedEpisode;

}
