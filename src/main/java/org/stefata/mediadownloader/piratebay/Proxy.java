package org.stefata.mediadownloader.piratebay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@JsonDeserialize(builder = Proxy.ProxyBuilder.class)
@Data
@Builder
public class Proxy {

    private final String domain;
    private final Boolean secure;
    private final String country;
    private final Boolean probed;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProxyBuilder {

    }

}
