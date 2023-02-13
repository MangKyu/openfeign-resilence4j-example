package com.mangkyu.openfeign.app.resttemplate;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Disabled
class ExchangeRateRestTemplateTest {

    @Autowired
    private ExchangeRateRestTemplate restTemplate;

    @Test
    void 환율조회() {
        ExchangeRateResponse result = restTemplate.call(Currency.USD, Currency.KRW);
        assertThat(result).isNotNull();

        log.info("result: {}", result);
    }

}