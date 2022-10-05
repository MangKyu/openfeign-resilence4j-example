package com.mangkyu.openfeign.app;

import lombok.*;

import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString
public class ExchangeRateResponse {

    private final long timestamp;
    private final Currency source;
    private final Map<String, Double> quotes;

}
