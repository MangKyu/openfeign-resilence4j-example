package com.mangkyu.openfeign.app.openfeign;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import com.mangkyu.openfeign.config.ExchangeRateProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ExchangeRateOpenFeignTest {

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