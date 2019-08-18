package org.stefata.mediadownloader.html;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@Slf4j
public class HtmlExtractor {

    public Document fromUrl(String url) {
        try {
            log.info("Extracting html from {}", url);
            return Jsoup.connect(url).get();
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
    }
}
