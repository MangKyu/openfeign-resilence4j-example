package com.mangkyu.openfeign.app.openfeign.circuit;

import feign.FeignException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ExceptionRecordFailurePredicateTest {

    @MethodSource("statusMethod")
    @ParameterizedTest
    void feignException(HttpStatus status, boolean isFail) {
        // given
        FeignException exception = mock(FeignException.class);
        doReturn(status.value())
                .when(exception)
                .status();

        // when
        boolean result = new ExceptionRecordFailurePredicate().test(exception);

        // then
        assertThat(result).isEqualTo(isFail);
    }

    @MethodSource("statusMethod")
    @ParameterizedTest
    void test(HttpStatus status, boolean isFail) {
        // given

        // when
        boolean result = new ExceptionRecordFailurePredicate().test(new RuntimeException());

        // then
        assertThat(result).isEqualTo(isFail);
    }

    private static List<Arguments> statusMethod() {
        return Arrays.asList(
                Arguments.of(HttpStatus.CONTINUE, false),
                Arguments.of(HttpStatus.OK, false),
                Arguments.of(HttpStatus.TEMPORARY_REDIRECT, false),
                Arguments.of(HttpStatus.FORBIDDEN, false),
                Arguments.of(HttpStatus.INTERNAL_SERVER_ERROR, true)
        );

    }

}