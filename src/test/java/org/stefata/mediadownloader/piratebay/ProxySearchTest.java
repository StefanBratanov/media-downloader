package org.stefata.mediadownloader.piratebay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProxySearch.class)
class ProxySearchTest {

    @Autowired
    private ProxySearch subject;

    @Test
    public void getsSingleProxy() {
        StepVerifier.create(subject.getProxy())
                .assertNext(proxy -> {
                    assertThat(proxy.getCountry()).isEqualTo("GB");
                    assertThat(proxy.getSecure()).isTrue();
                    assertThat(proxy.getDomain()).isNotNull();
                })
                .verifyComplete();
    }

}