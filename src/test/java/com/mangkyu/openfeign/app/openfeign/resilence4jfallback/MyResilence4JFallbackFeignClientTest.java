package com.mangkyu.openfeign.app.openfeign.resilence4jfallback;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
//import com.mangkyu.openfeign.app.openfeign.feignfallback.MyFeignFallbackFeignClient;
import com.mangkyu.openfeign.config.ExchangeRateProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class MyResilence4JFallbackFeignClientTest {

    @Autowired
    private MyResilence4jFallbackFeignClient openFeign;
    @Autowired
    private ExchangeRateProperties properties;

    @Test
    void defaultValueCallback() {
        ExchangeRateResponse result = openFeign.defaultValueCallback(properties.getKey(), Currency.JPY, Currency.KRW);
        assertThat(result).isNotNull();
    }

    @Test
    void throwsExceptionCallback() {
        assertThatThrownBy(() -> openFeign.throwsExceptionCallback(properties.getKey(), Currency.JPY, Currency.KRW))
                .isInstanceOf(NoFallbackAvailableException.class);
    }

}