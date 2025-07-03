package com.clinio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinioApplication.class, args);
    }
}
