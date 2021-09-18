package com.example.HR;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
    @EnableScheduling
    public class SchedulingConfiguration {

    }
}

