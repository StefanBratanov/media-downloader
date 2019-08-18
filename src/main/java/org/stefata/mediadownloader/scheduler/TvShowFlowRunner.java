package org.stefata.mediadownloader.scheduler;

import de.jkeylockmanager.manager.KeyLockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.stefata.mediadownloader.flow.TvShowFlow;
import org.stefata.mediadownloader.persistence.repository.TvShowRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TvShowFlowRunner {

    private final TvShowRepository tvShowRepository;
    private final TvShowFlow tvShowFlow;
    private final KeyLockManager keyLockManager;

    @Scheduled(cron = "${flow.run.cron}")
    public void runTvShowFlow() {
        log.info("Scheduled to run flow for all shows");
        tvShowRepository.findAll()
                .forEach(tvShow ->
                        keyLockManager.executeLocked(tvShow,
                                () -> tvShowFlow.runFlow(tvShow)));
    }
}
