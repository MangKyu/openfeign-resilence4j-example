package com.mangkyu.openfeign.app.openfeign.feignfallback;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;

class MyFallbackWithFactory implements MyFeignFallbackFeignClient {


    @Override
    public ExchangeRateResponse defaultValueCallback(String apiKey, Currency source, Currency currencies) {
        return ExchangeRateResponse.builder().build();
    }

    @Override
    public ExchangeRateResponse throwsExceptionCallback(String apiKey, Currency source, Currency currencies) {
        throw new ArrayIndexOutOfBoundsException();
    }
}
