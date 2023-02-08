package com.mangkyu.openfeign.app.openfeign.feignfallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
class MyTestFallbackFactory implements FallbackFactory<MyFallbackWithFactory> {

    @Override
    public MyFallbackWithFactory create(Throwable cause) {
        return new MyFallbackWithFactory();
    }

}
