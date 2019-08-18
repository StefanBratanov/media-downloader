package org.stefata.mediadownloader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestResources {

    public static String readResource(String resource) throws URISyntaxException {
        try {
            return Files.readString(
                    Paths.get(TestResources.class.getResource(resource)
                            .toURI()));
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
    }
}
