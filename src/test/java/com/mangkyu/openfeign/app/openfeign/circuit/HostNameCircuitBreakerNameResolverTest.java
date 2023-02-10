package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.Target;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class HostNameCircuitBreakerNameResolverTest {

    @MethodSource("circuitBreakerName")
    @ParameterizedTest
    void circuitBreakerName검사(String originUrl, String circuitBreakerName) {
        // given
        Target<?> target = mock(Target.class);
        doReturn(originUrl)
                .when(target)
                .url();

        // when
        String result = new HostNameCircuitBreakerNameResolver().resolveCircuitBreakerName(null, target, null);

        // then
        assertThat(result).isEqualTo(circuitBreakerName);
    }

    public static List<Arguments> circuitBreakerName() {
        return Arrays.asList(
                Arguments.of("https://www.naver.com/search", "www.naver.com"),
                Arguments.of("https://google.com/search?name=MangKyu", "google.com"),
                Arguments.of("http://www.naver.com/search?name=MangKyu&key=MangKyu", "www.naver.com"),
                Arguments.of("http://google.com/search?name=MangKyu", "google.com"),
                Arguments.of("localhost:8080/test", "default"),
                Arguments.of("http://localhost:8080/test", "localhost"),
                Arguments.of("http://127.0.0.1:8080/test", "127.0.0.1"),
                Arguments.of("127.0.0.1:8080/test", "default")
        );
    }

}