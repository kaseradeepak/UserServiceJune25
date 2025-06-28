package com.scaler.userservicejune25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserServiceJune25Application {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceJune25Application.class, args);
    }

}
