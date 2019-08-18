package org.stefata.mediadownloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;

@Entity
@Table(name = "TV_SHOW_TORRENTS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TvShowTorrent {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TV_SHOW_TITLE", referencedColumnName = "TITLE")
    private TvShow tvShow;

    @Column(name = "TORRENT_TITLE")
    private String torrentTitle;

    @Column(name = "SEASON")
    private Integer season;

    @Column(name = "EPISODE")
    private Integer episode;

    @Column(name = "TORRENT_URL")
    private URL torrentUrl;

}
