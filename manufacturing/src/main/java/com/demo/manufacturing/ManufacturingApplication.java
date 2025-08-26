package com.demo.manufacturing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.demo.core", "com.demo.manufacturing"})
public class ManufacturingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManufacturingApplication.class, args);
    }

}