package org.stefata.mediadownloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.stefata.mediadownloader.persistence.repository.TvShowControlTableRepository;
import org.stefata.mediadownloader.persistence.repository.TvShowRepository;
import org.stefata.mediadownloader.persistence.repository.TvShowTorrentRepository;

@ActiveProfiles("test")
@Sql(scripts = "classpath:cleanup.sql")
public class DatabaseIT {

    @Autowired
    protected TvShowControlTableRepository tvShowControlTableRepository;
    @Autowired
    protected TvShowRepository tvShowRepository;
    @Autowired
    protected TvShowTorrentRepository tvShowTorrentRepository;

}
