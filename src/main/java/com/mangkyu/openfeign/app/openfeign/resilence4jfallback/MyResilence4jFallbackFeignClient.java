package com.mangkyu.openfeign.app.openfeign.resilence4jfallback;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "Resilence4jFallbackFeignClient",
        url = "${exchange.currency.api.uri}"
)
interface MyResilence4jFallbackFeignClient {

    @GetMapping
    @CircuitBreaker(name = "currency", fallbackMethod = "defaultValueCallbackImplements")
    ExchangeRateResponse defaultValueCallback(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

    default ExchangeRateResponse defaultValueCallbackImplements(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies,
            Throwable e) {

        return ExchangeRateResponse.builder().build();
    }

    @GetMapping
    @CircuitBreaker(name = "currency", fallbackMethod = "throwsExceptionCallbackImplements")
    ExchangeRateResponse throwsExceptionCallback(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

    default ExchangeRateResponse throwsExceptionCallbackImplements(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies,
            Throwable e) {
        throw new ArrayStoreException();
    }

}