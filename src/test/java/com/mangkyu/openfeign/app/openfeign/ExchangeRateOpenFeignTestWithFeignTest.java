package com.mangkyu.openfeign.app.openfeign;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mangkyu.openfeign.feigntest.cloudconfig.FeignTest;
import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.config.ExchangeRateProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@FeignTest
class ExchangeRateOpenFeignTestWithFeignTest {

    @Autowired
    private ExchangeRateOpenFeign openFeign;
    @Autowired
    private ExchangeRateProperties properties;

    @Test
    void 환율조회() {
        ExchangeRateResponse result = openFeign.call(properties.getKey(), Currency.USD, Currency.KRW);
        assertThat(result).isNotNull();

        log.info("result: {}", result);
    }

}