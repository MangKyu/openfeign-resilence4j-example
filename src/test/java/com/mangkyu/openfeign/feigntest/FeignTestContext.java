package com.mangkyu.openfeign.feigntest;

import com.mangkyu.openfeign.config.CustomPropertiesConfig;
import com.mangkyu.openfeign.config.OpenFeignConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ImportAutoConfiguration({
        OpenFeignConfig.class,
        CustomPropertiesConfig.class,
        FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        ConfigurationPropertiesAutoConfiguration.class
})
class FeignTestContext {

}