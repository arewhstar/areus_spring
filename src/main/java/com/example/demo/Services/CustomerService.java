package com.example.demo.Services;
import com.example.demo.Entites.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer save(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findOne(Long id);

    boolean isExists(Long id);

    Customer partialUpdate(Long id, Customer customer);

    void delete(Long id);

    Double getAverageAge();

    public List<Customer> findYoungCustomers();
}