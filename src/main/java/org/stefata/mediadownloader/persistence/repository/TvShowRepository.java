package org.stefata.mediadownloader.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stefata.mediadownloader.persistence.model.TvShow;

public interface TvShowRepository extends JpaRepository<TvShow,Long> {

}
