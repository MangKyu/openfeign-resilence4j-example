package com.mangkyu.openfeign.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("exchange.currency.api")
@RequiredArgsConstructor
public class ExchangeRateProperties {

    private final String uri;
    private final String key;

}