/*
 *
 *  TestController.java 2023-02-07
 *
 *  Copyright 2023 WorksMobile Corp. All rights Reserved.
 *  WorksMobile PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.mangkyu.openfeign.app.openfeign;

import com.mangkyu.openfeign.app.Currency;
import com.mangkyu.openfeign.app.ExchangeRateResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Slf4j
class CircuitBreakerTestController {

    private final ExchangeRateOpenFeign feign;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/circuit/call")
    public ResponseEntity<ExchangeRateResponse> call() {
        ExchangeRateResponse response = feign.call("asd", Currency.USD, Currency.KRW);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/circuit/call2")
    public ResponseEntity<ExchangeRateResponse> call2() {
        ExchangeRateResponse response = feign.call2("asd", Currency.USD, Currency.KRW);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/circuit/close")
    public ResponseEntity<Void> close(@RequestParam String name) {
        circuitBreakerRegistry.circuitBreaker(name)
                .transitionToClosedState();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/circuit/open")
    public ResponseEntity<Void> open(@RequestParam String name) {
        circuitBreakerRegistry.circuitBreaker(name)
                .transitionToOpenState();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/circuit/status")
    public ResponseEntity<CircuitBreaker.State> status(@RequestParam String name) {
        CircuitBreaker.State state = circuitBreakerRegistry.circuitBreaker(name)
                .getState();
        return ResponseEntity.ok(state);
    }

    @GetMapping("/circuit/all")
    public ResponseEntity<Void> all() {
        Seq<CircuitBreaker> circuitBreakers = circuitBreakerRegistry.getAllCircuitBreakers();
        for (CircuitBreaker circuitBreaker : circuitBreakers) {
            log.error("circuitName={}, state={}", circuitBreaker.getName(), circuitBreaker.getState());
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("code", "FeignException"));
    }

    @ExceptionHandler(NoFallbackAvailableException.class)
    public ResponseEntity<?> handleNoFallbackAvailableException(NoFallbackAvailableException e) {
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("code", "NoFallbackAvailableException"));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<?> handleCallNotPermittedException(CallNotPermittedException e) {
        return ResponseEntity.internalServerError()
                .body(Collections.singletonMap("code", "CallNotPermittedException"));
    }

}
