package org.stefata.mediadownloader.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stefata.mediadownloader.persistence.model.TvShowTorrent;

public interface TvShowTorrentRepository extends JpaRepository<TvShowTorrent,Long> {
}
