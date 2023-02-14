package com.mangkyu.openfeign.app.openfeign.fallback.feignfallback.method;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.app.openfeign.fallback.feignfallback.factory.MyTestFallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "MyFeignFallbackMethodFeignClient",
        url = "${exchange.currency.api.uri}",
        fallback = MyFeignFallbackMethod.class
)
interface MyFeignFallbackMethodFeignClient {

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

@Component
@Slf4j
class MyFeignFallbackMethod implements MyFeignFallbackMethodFeignClient {


    @Override
    public ExchangeRateResponse defaultValueCallback(String apiKey, Currency source, Currency currencies) {
        log.error("defaultValueCallback called");
        return ExchangeRateResponse.builder().build();
    }

    @Override
    public ExchangeRateResponse throwsExceptionCallback(String apiKey, Currency source, Currency currencies) {
        log.error("throwsExceptionCallback called");
        throw new ArrayIndexOutOfBoundsException();
    }
}