package com.example.demo;

import com.example.demo.Entites.Customer;
import com.example.demo.Repositories.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(@Autowired CustomerRepository customerRepository,@Autowired Environment env) {
		return args -> {
			if (!List.of(env.getActiveProfiles()).contains("test")) {
				if (customerRepository.count() == 0) {

					List<Customer> customers = List.of(
							new Customer(null, "John", "Doe", 30, "123 Main St"),
							new Customer(null, "Jane", "Smith", 25, "456 Oak St"),
							new Customer(null, "Mike", "Brown", 19, "789 Maple Ave"),
							new Customer(null, "Alice", "Johnson", 40, "987 Pine Rd"),
							new Customer(null, "Robert", "Wilson", 22, "321 Cedar Blvd")
					);
					customerRepository.saveAll(customers);
					System.out.println("📌 Customers are initialized");
				}
			}
		};
	}

}
