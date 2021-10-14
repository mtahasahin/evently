package com.github.mtahasahin.evently;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EventlyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventlyApplication.class, args);
    }
}
