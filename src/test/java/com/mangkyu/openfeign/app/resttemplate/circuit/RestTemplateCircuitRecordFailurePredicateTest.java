package com.mangkyu.openfeign.app.resttemplate.circuit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RestTemplateCircuitRecordFailurePredicateTest {

    @MethodSource("failureException")
    @ParameterizedTest
    void failureException(Throwable throwable) {
        // given

        // when
        boolean result = new RestTemplateCircuitRecordFailurePredicate().test(throwable);

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    void httpClientErrorException() {
        // given

        // when
        boolean result = new RestTemplateCircuitRecordFailurePredicate().test(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // then
        assertThat(result).isEqualTo(false);
    }

    private static List<Arguments> failureException() {
        return Arrays.asList(
                Arguments.of(mock(HttpServerErrorException.class)),
                Arguments.of(mock(TimeoutException.class)),
                Arguments.of(mock(ConnectException.class)),
                Arguments.of(mock(IOException.class))
        );
    }

}