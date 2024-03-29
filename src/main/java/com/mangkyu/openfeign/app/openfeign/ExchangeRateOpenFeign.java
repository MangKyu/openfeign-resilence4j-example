package com.mangkyu.openfeign.app.openfeign;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ExchangeRateOpenFeign", url = "${exchange.currency.api.uri}")
//@CircuitBreaker
public interface ExchangeRateOpenFeign {

    @GetMapping
    ExchangeRateResponse call(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

    @GetMapping
    ExchangeRateResponse call2(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

}