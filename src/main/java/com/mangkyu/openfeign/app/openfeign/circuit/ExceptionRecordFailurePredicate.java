package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.FeignException;
import org.springframework.http.HttpStatus;

import java.util.function.Predicate;

public class ExceptionRecordFailurePredicate implements Predicate<Throwable> {

    // 반환값이 True면 Fail로 기록됨
    @Override
    public boolean test(Throwable t) {
        if (t instanceof FeignException) {
            int status = ((FeignException) t).status();
            return HttpStatus.valueOf(status).is5xxServerError();
        }

        return false;
    }

}
