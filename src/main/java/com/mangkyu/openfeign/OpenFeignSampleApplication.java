package com.mangkyu.openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableFeignClients
@SpringBootApplication
public class OpenFeignSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenFeignSampleApplication.class, args);
    }

}
