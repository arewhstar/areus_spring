package com.example.demo.Config;

import com.example.demo.Repositories.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    CommandLineRunner disableDataInit(CustomerRepository customerRepository) {
        return args -> {

        };
    }
}