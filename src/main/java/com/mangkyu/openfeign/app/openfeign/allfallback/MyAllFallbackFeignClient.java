package com.mangkyu.openfeign.app.openfeign.allfallback;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.app.openfeign.feignfallback.MyTestFallbackFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "MyAllFallbackFeignClient",
        url = "${exchange.currency.api.uri}",
        fallbackFactory = MyTestFallbackFactory.class
)
public interface MyAllFallbackFeignClient {

    Logger log = LoggerFactory.getLogger(MyAllFallbackFeignClient.class);

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
        log.error("throwsExceptionCallbackImplements called");
        throw new ArrayStoreException();
    }

}