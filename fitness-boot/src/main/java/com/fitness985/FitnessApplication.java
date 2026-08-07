package com.fitness985;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.fitness985", "com.fitnessuser"})
public class FitnessApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessApplication.class, args);
    }
}
