package org.stefata.mediadownloader.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stefata.mediadownloader.persistence.model.TvShowControlTable;

import java.util.Optional;

public interface TvShowControlTableRepository extends JpaRepository<TvShowControlTable, Long> {

    Optional<TvShowControlTable> findByTvShow_Title(String title);

}
