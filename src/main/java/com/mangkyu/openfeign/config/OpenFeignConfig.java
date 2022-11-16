/*
 *
 *  OpenFeignConfig.java 2022-09-15
 *
 *  Copyright 2022 WorksMobile Corp. All rights Reserved.
 *  WorksMobile PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.mangkyu.openfeign.config;

import static feign.Logger.Level.*;

import feign.*;
import lombok.extern.slf4j.Slf4j;

import org.bouncycastle.util.Arrays;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients("com.mangkyu.openfeign")
public class OpenFeignConfig {

    /**
     * OpenFeign 12.0 버전에 버그픽스 되었으므로 버전 확인 후에 적용 필요!
     * 관련 문서: https://github.com/OpenFeign/feign/releases/tag/12.0
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            if(Arrays.isNullOrEmpty(requestTemplate.body()) && !isGetOrDelete(requestTemplate)) {
                // body가 비어있는 경우에 요청을 보내면 411 에러가 생김 https://github.com/OpenFeign/feign/issues/1251
                // content-length로 처리가 안되어서 빈 값을 항상 보내주도록 함
                requestTemplate.body("{}");
            }
        };
    }

    private boolean isGetOrDelete(RequestTemplate requestTemplate) {
        return Request.HttpMethod.GET.name().equals(requestTemplate.method())
                || Request.HttpMethod.DELETE.name().equals(requestTemplate.method());
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return BASIC;
    }
    
    @Bean
    public FeignFormatterRegistrar dateTimeFormatterRegistrar() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }
    
    @Bean
    public CustomFeignRequestLogging customFeignRequestLogging() {
        return new CustomFeignRequestLogging();
    }

    @Slf4j
    static class CustomFeignRequestLogging extends Logger {

        private final ThreadLocal<String> requestId = new ThreadLocal<>();

        @Override
        protected void logRequest(String configKey, Level logLevel, Request request) {
            if (logLevel.ordinal() >= HEADERS.ordinal()) {
                super.logRequest(configKey, logLevel, request);
            } else {
                requestId.set(Integer.toString((int) (Math.random() * 1000000)));
                String stringBody = createRequestStringBody(request);
                log.info("[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
                    requestId.get(), request.url(), request.httpMethod(), request.headers(), stringBody);
            }
        }

        private String createRequestStringBody(Request request) {
            return request.body() == null
                ? ""
                : new String(request.body(), StandardCharsets.UTF_8);
        }

        @Override
        protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws
            IOException {
            if (logLevel.ordinal() >= HEADERS.ordinal()) {
                super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
                return response;
            } else {
                byte[] byteArray = getResponseBodyByteArray(response);
                log.info("[{}] Status: {}, Body:{} ",
                    requestId.get(), HttpStatus.valueOf(response.status()), new String(byteArray, StandardCharsets.UTF_8));
                return response.toBuilder().body(byteArray).build();
            }
        }

        private byte[] getResponseBodyByteArray(Response response) throws IOException {
            if (response.body() == null) {
                return new byte[]{};
            }

            return StreamUtils.copyToByteArray(response.body().asInputStream());
        }


        @Override
        protected void log(String configKey, String format, Object... args) {
            log.debug(format(configKey, format, args));
        }

        protected String format(String configKey, String format, Object... args) {
            return String.format(methodTag(configKey) + format, args);
        }
    }

    @Bean
    Retryer.Default retryer() {
        // 0.1초의 간격으로 시작해 최대 3초의 간격으로 점점 증가하며, 최대5번 재시도한다.
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(3L), 5);
    }
}
