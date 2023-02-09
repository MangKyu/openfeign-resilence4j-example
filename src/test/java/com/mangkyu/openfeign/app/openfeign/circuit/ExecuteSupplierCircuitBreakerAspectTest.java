package com.mangkyu.openfeign.app.openfeign.circuit;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.cloud.openfeign.FeignClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecuteSupplierCircuitBreakerAspectTest {

    @InjectMocks
    private ExecuteSupplierCircuitBreakerAspect aspect;

    @Mock
    private CircuitBreakerRegistry registry;

    @Mock
    private CircuitBreaker circuitBreaker;

    @Test
    void 서킷브레이커실행_예외발생() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        DefaultFeignClient target = new DefaultFeignClient();
        doReturn(target)
                .when(joinPoint)
                .getTarget();

        doReturn(circuitBreaker)
                .when(registry)
                .circuitBreaker("default");

        doThrow(new NoFallbackAvailableException("ashdo", new IllegalArgumentException()))
                .when(circuitBreaker)
                .executeCheckedSupplier(any());

        // when
        assertThatThrownBy(() -> aspect.circuitBreakerDefaultAdvice(joinPoint, mock(FeignClient.class)))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        verify(circuitBreaker, times(1)).executeCheckedSupplier(any());
    }

    @Test
    void 서킷브레이커실행_성공_기본FeignClient() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        DefaultFeignClient target = new DefaultFeignClient();
        doReturn(target)
                .when(joinPoint)
                .getTarget();

        doReturn(circuitBreaker)
                .when(registry)
                .circuitBreaker("default");

        // when
        aspect.circuitBreakerDefaultAdvice(joinPoint, mock(FeignClient.class));

        // then
        verify(circuitBreaker, times(1)).executeCheckedSupplier(any());
    }

    @Test
    void 서킷브레이커실행_성공_커스텀FeignClient() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        FeignClientWithFeignCircuitBreaker target = new FeignClientWithFeignCircuitBreaker();
        doReturn(target)
                .when(joinPoint)
                .getTarget();

        doReturn(circuitBreaker)
                .when(registry)
                .circuitBreaker("MangKyu");

        // when
        aspect.circuitBreakerDefaultAdvice(joinPoint, mock(FeignClient.class));

        // then
        verify(circuitBreaker, times(1)).executeCheckedSupplier(any());
    }


    @FeignClient
    @FeignCircuitBreaker("MangKyu")
    static class FeignClientWithFeignCircuitBreaker {

    }

    @FeignClient
    static class DefaultFeignClient {

    }

}