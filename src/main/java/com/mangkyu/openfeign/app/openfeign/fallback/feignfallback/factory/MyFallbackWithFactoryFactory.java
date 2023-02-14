package com.mangkyu.openfeign.app.openfeign.fallback.feignfallback.factory;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.app.openfeign.fallback.allfallback.MyAllFallbackFeignClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MyFallbackWithFactoryFactory implements MyFeignFallbackFactoryFeignClient, MyAllFallbackFeignClient {


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
