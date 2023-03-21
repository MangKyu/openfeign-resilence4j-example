package com.mangkyu.openfeign.app.resttemplate.circuit;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.config.ExchangeRateProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
class AnnotationBaseCircuitExchangeRateRestTemplate {

    private final RestTemplate restTemplate;
    private final ExchangeRateProperties properties;
    private static final String API_KEY = "apikey";

    @CircuitBreaker(name = "exchangeRate")
    public ExchangeRateResponse call(final Currency source, final Currency target) {

        return restTemplate.exchange(
                        createApiUri(source, target),
                        HttpMethod.GET,
                        new HttpEntity<>(createHttpHeaders()),
                        ExchangeRateResponse.class)
                .getBody();
    }

    private String createApiUri(final Currency source, final Currency target) {
        return UriComponentsBuilder.fromHttpUrl(properties.getUri())
                .queryParam("source", source.name())
                .queryParam("currencies", target.name())
                .encode()
                .toUriString();
    }

    private HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY, properties.getKey());
        return headers;
    }

}