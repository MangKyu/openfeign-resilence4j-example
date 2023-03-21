package com.mangkyu.openfeign.app.resttemplate.circuit;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestTemplateCircuitBreakerAspectTest {

    @InjectMocks
    private RestTemplateCircuitBreakerAspect target;

    @Mock
    private CircuitBreakerRegistry registry;

    @ParameterizedTest
    @MethodSource("validUrl")
    void URL을_HostName으로_파싱_테스트(String url, String hostName) throws Throwable {
        //given
        final ProceedingJoinPoint mockPjp = mock(ProceedingJoinPoint.class);
        final CircuitBreaker mockCircuitBreaker = mock(CircuitBreaker.class);

        doReturn(mockCircuitBreaker)
                .when(registry)
                .circuitBreaker(hostName);
        doReturn("result")
                .when(mockCircuitBreaker)
                .executeCheckedSupplier(any());

        //when
        target.aspect(mockPjp, url);

        //then
        verify(registry.circuitBreaker(hostName))
                .executeCheckedSupplier(any());
    }

    private static Stream<Arguments> validUrl() {
        return Stream.of(
                Arguments.of("http://www.naver.com", "www.naver.com"),
                Arguments.of("http://www.naver.com:8080", "www.naver.com"),
                Arguments.of("https://www.kangho.com:99/path/path", "www.kangho.com"),
                Arguments.of("https://localhost:9080/path?query=param", "localhost")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUrl")
    void URL_HostName으로_파싱_실패(String url) throws Throwable {
        //given
        final ProceedingJoinPoint mockPjp = mock(ProceedingJoinPoint.class);
        final CircuitBreaker mockCircuitBreaker = mock(CircuitBreaker.class);

        doReturn(mockCircuitBreaker)
                .when(registry)
                .circuitBreaker("default");
        doReturn("result")
                .when(mockCircuitBreaker)
                .executeCheckedSupplier(any());

        //when
        target.aspect(mockPjp, url);

        //then
        verify(registry.circuitBreaker("default"))
                .executeCheckedSupplier(any());
    }

    private static Stream<Arguments> invalidUrl() {
        return Stream.of(
                Arguments.of("www.naver.com"),
                Arguments.of("")
                // Arguments.of("http:/localhost") // MalformedURLException 오류 발생하지 않고 ""으로 파싱됨
        );
    }
}