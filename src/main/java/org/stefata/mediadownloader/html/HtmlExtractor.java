package org.stefata.mediadownloader.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class HtmlExtractor {

    public Document fromUrl(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
    }
}
