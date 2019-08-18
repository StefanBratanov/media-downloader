package org.stefata.mediadownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediaDownloaderApp {

    public static void main(String[] args) {
        SpringApplication.run(MediaDownloaderApp.class, args);
    }

}
