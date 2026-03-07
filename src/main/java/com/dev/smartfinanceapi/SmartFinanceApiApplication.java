package com.dev.smartfinanceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartFinanceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFinanceApiApplication.class, args);
    }

}
