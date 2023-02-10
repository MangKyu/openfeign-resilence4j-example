package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.FeignException;
import feign.RetryableException;

import java.util.function.Predicate;

public class ExceptionRecordFailurePredicate implements Predicate<Throwable> {

    // 반환값이 True면 Fail로 기록됨
    @Override
    public boolean test(Throwable t) {
        if (t instanceof RetryableException) {
            return true;
        }

        return t instanceof FeignException.FeignServerException;
    }

}
