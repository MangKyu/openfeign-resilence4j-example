package com.mangkyu.openfeign.feigntest.cloudconfig;

import com.mangkyu.openfeign.config.CustomPropertiesConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@EnableFeignClients(basePackages = "com.mangkyu.openfeign")
@ImportAutoConfiguration({
		CustomPropertiesConfig.class,
		FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
		ConfigurationPropertiesAutoConfiguration.class
})
class FeignTestContext {
}