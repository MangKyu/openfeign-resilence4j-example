package com.mangkyu.openfeign.app.openfeign.feignfallback;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ExchangeRateFeignFallbackFeignClient",
        url = "${exchange.currency.api.uri}",
        fallbackFactory = MyTestFallbackFactory.class
)
interface MyFeignFallbackFeignClient {

    @GetMapping
    ExchangeRateResponse defaultValueCallback(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

    @GetMapping
    ExchangeRateResponse throwsExceptionCallback(
            @RequestHeader String apiKey,
            @RequestParam Currency source,
            @RequestParam Currency currencies);

}