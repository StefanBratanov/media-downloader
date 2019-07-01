package org.stefata.mediadownloader.piratebay;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProxySearch {

    @Value("${piratebay.proxies.url}")
    private String pirateBayProxiesUrl;

    public Mono<Proxy> getProxy() {
        return WebClient.create(pirateBayProxiesUrl)
                .get()
                .uri("/api/v1/proxies")
                .retrieve()
                .bodyToMono(ProxyResponse.class)
                .map(ProxyResponse::getProxies)
                .flatMapMany(Flux::fromIterable)
                .filter(Proxy::getSecure)
                .filter(p -> p.getCountry().equals("GB"))
                .next();
    }

    @NoArgsConstructor
    @Data
    private static class ProxyResponse {
        private List<Proxy> proxies;
    }

}
