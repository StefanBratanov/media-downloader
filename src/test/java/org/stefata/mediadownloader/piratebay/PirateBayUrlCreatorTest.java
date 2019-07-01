package org.stefata.mediadownloader.piratebay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PirateBayUrlCreator.class)
class PirateBayUrlCreatorTest {

    @Autowired
    private PirateBayUrlCreator subject;

    @Test
    public void createsSearchUrl() {
        var pirateBayUrl = "unblockpirate.uk";
        String result = subject.createSearchUrl(pirateBayUrl, "How i met your mother");
        assertThat(result).isEqualTo("https://unblockpirate.uk/search.php?q=How+i+met+your+mother");
    }

}