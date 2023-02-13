package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class DefaultExceptionRecordFailurePredicateTest {

    @MethodSource("failureException")
    @ParameterizedTest
    void failureException(Throwable throwable) {
        // given

        // when
        boolean result = new DefaultExceptionRecordFailurePredicate().test(throwable);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    void runtimeException() {
        // given

        // when
        boolean result = new DefaultExceptionRecordFailurePredicate().test(new RuntimeException());

        // then
        assertThat(result).isEqualTo(false);
    }

    private static List<Arguments> failureException() {
        return Arrays.asList(
                Arguments.of(mock(RetryableException.class)),
                Arguments.of(mock(TimeoutException.class)),
                Arguments.of(mock(FeignException.FeignServerException.class))
        );

    }

}