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
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
class CircuitBreakerTestController {

    private final ExchangeRateOpenFeign feign;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/circuit/call")
    public ResponseEntity<ExchangeRateResponse> call() {
        ExchangeRateResponse response = feign.call("asd", Currency.USD, Currency.KRW);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/circuit/close")
    public ResponseEntity<Void> close() {
        circuitBreakerRegistry.circuitBreaker("default")
                .transitionToClosedState();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/circuit/open")
    public ResponseEntity<Void> open() {
        circuitBreakerRegistry.circuitBreaker("default")
                .transitionToOpenState();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("code", "FeignException"));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<?> handleCallNotPermittedException(CallNotPermittedException e) {
        return ResponseEntity.internalServerError()
                .body(Collections.singletonMap("code", "CallNotPermittedException"));
    }

}
