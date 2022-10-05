/*
 *
 *  WebMvcTestConfig.java 2022-09-16
 *
 *  Copyright 2022 WorksMobile Corp. All rights Reserved.
 *  WorksMobile PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.mangkyu.openfeign.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest
class WebMvcTestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mockMvc가Null이아님() {
        assertThat(mockMvc).isNotNull();
    }

}
