package com.mangkyu.openfeign.app.openfeign.fallback.feignfallback.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MyTestFallbackFactory implements FallbackFactory<MyFallbackWithFactoryFactory> {

    @Override
    public MyFallbackWithFactoryFactory create(Throwable cause) {
        return new MyFallbackWithFactoryFactory();
    }

}
