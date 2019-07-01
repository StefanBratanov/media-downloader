package org.stefata.mediadownloader.html;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HtmlExtractor.class)
class HtmlExtractorTest {

    @Autowired
    private HtmlExtractor subject;

    @Test
    public void extractsHtmlFromUrl() {
        Document document = subject.fromUrl("https://www.groupon.co.uk/");
        assertThat(document.html()).isNotNull();
    }

}