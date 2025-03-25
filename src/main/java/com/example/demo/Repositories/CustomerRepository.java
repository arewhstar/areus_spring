package com.example.demo.Repositories;

import com.example.demo.Entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT AVG(c.age) FROM Customer c")
    Double findAverageAge();
}
