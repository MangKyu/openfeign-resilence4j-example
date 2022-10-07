package com.mangkyu.openfeign.feigntest.cloudconfig;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@EnableFeignClients(basePackages = "com.mangkyu.openfeign")
@ImportAutoConfiguration({
        FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
		FeignContextPropertiesConfig.class,
})
class FeignTestContext {
}