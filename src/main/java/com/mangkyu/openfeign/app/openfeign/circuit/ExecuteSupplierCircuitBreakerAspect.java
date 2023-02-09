package com.mangkyu.openfeign.app.openfeign.circuit;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class ExecuteSupplierCircuitBreakerAspect {

    private final CircuitBreakerRegistry registry;

    @Around("@within(feignClient)")
    public Object circuitBreakerDefaultAdvice(ProceedingJoinPoint joinPoint, FeignClient feignClient) throws Throwable {
        String circuitBreakerName = findCircuitBreakerName(joinPoint);
        CircuitBreaker circuitBreaker = registry.circuitBreaker(circuitBreakerName);

        try {
            return circuitBreaker.executeCheckedSupplier(joinPoint::proceed);
        } catch (NoFallbackAvailableException e) {
            throw Optional.ofNullable(NestedExceptionUtils.getRootCause(e))
                    .orElse(e);
        }
    }

    private String findCircuitBreakerName(ProceedingJoinPoint joinPoint) {
        return Optional.ofNullable(AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), FeignCircuitBreaker.class))
                .map(FeignCircuitBreaker::value)
                .orElse("default");
    }

}
