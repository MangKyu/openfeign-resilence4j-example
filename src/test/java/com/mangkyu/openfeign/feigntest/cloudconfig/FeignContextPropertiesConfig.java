/*
 *
 *  PropertiesConfigForFeign.java 2022-10-07
 *
 *  Copyright 2022 WorksMobile Corp. All rights Reserved.
 *  WorksMobile PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.mangkyu.openfeign.feigntest.cloudconfig;

import com.mangkyu.openfeign.config.CustomPropertiesConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

@Configuration
@Conditional(SpringCloudNotExistsCondition.class)
@ImportAutoConfiguration({
        ConfigurationPropertiesAutoConfiguration.class,
        CustomPropertiesConfig.class
})
class FeignContextPropertiesConfig {

}

class SpringCloudNotExistsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String springCloudConfigUrl = conditionContext.getEnvironment().getProperty("spring.config.import");
        return !StringUtils.hasText(springCloudConfigUrl);
    }

}