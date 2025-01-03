package com.ripple.BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RippleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RippleApplication.class, args);
    }
}
