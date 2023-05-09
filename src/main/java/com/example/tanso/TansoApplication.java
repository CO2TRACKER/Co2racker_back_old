package com.example.tanso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories("com.example.tanso")
@EntityScan("com.example.tanso")
@EnableScheduling
public class TansoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TansoApplication.class, args);
    }
}
