package org.stefata.mediadownloader.scheduler;

import de.jkeylockmanager.manager.KeyLockManager;
import de.jkeylockmanager.manager.KeyLockManagers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    @Bean
    public KeyLockManager keyLockManager() {
        return KeyLockManagers.newLock();
    }
}
