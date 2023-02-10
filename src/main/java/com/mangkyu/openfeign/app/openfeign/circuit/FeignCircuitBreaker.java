package com.mangkyu.openfeign.app.openfeign.circuit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignCircuitBreaker {

    String value() default "";

    boolean apply() default true;
}
