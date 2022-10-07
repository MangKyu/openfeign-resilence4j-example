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

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients("com.mangkyu.openfeign")
class OpenFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return BASIC;
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
