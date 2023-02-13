package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.FeignException;
import feign.RetryableException;

import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class DefaultExceptionRecordFailurePredicate implements Predicate<Throwable> {

    // 반환값이 True면 Fail로 기록됨
    @Override
    public boolean test(Throwable t) {
        // occurs in @CircuitBreaker TimeLimiter
        if (t instanceof TimeoutException) {
            return true;
        }

        // occurs in @OpenFeign
        if (t instanceof RetryableException) {
            return true;
        }

        return t instanceof FeignException.FeignServerException;
    }

}
