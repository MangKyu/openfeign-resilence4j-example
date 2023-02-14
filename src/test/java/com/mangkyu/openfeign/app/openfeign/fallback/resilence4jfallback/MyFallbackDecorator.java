package com.mangkyu.openfeign.app.openfeign.fallback.resilence4jfallback;

import io.github.resilience4j.fallback.FallbackDecorator;
import io.github.resilience4j.fallback.FallbackMethod;
import io.github.resilience4j.timelimiter.configure.IllegalReturnTypeException;
import io.vavr.CheckedFunction0;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.core.NestedExceptionUtils;

import java.util.Optional;

public class MyFallbackDecorator implements FallbackDecorator {

    @Override
    public boolean supports(Class<?> target) {
        return true;
    }

    @Override
    public CheckedFunction0<Object> decorate(FallbackMethod fallbackMethod, CheckedFunction0<Object> supplier) {
        return () -> {
            try {
                return supplier.apply();
            } catch (IllegalReturnTypeException e) {
                throw e;
            } catch (Throwable throwable) {
                return returnWithUnwrappedException(fallbackMethod, throwable);
            }
        };
    }

    private Object returnWithUnwrappedException(FallbackMethod fallbackMethod, Throwable throwable) throws Throwable {
        try {
            return fallbackMethod.fallback(throwable);
        } catch (NoFallbackAvailableException e) {
            throw Optional.ofNullable(NestedExceptionUtils.getRootCause(e))
                    .orElse(e);
        }
    }
}
