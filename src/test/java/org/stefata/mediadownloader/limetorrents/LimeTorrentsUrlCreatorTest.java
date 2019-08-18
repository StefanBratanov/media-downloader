package org.stefata.mediadownloader.limetorrents;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LimeTorrentsUrlCreator.class)
class LimeTorrentsUrlCreatorTest {

    @Autowired
    private LimeTorrentsUrlCreator subject;

    @Test
    public void createsUrl() {
        String domain = "https://www.limetorrents.info";
        String title = "Stranger things";

        String expectedUrl = "https://www.limetorrents.info/search/all/Stranger+things";

        String result = subject.createSearchUrl(domain, title);

        assertThat(result).isEqualTo(expectedUrl);
    }

}