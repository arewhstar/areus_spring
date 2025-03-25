package com.example.demo.Services.Impl;
import com.example.demo.Entites.Customer;
import com.example.demo.Repositories.CustomerRepository;
import com.example.demo.Services.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer authorEntity) {
        return customerRepository.save(authorEntity);
    }

    @Override
    public List<Customer> findAll() {
        return StreamSupport.stream(customerRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findOne(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public Customer partialUpdate(Long id, Customer customer) {
        customer.setId(id);

        return customerRepository.findById(id).map(existingCustomer -> {
            Optional.ofNullable(customer.getFirstname()).ifPresent(existingCustomer::setFirstname);
            Optional.ofNullable(customer.getLastname()).ifPresent(existingCustomer::setLastname);
            Optional.of(customer.getAge()).ifPresent(existingCustomer::setAge);
            return customerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer does not exist"));
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Double getAverageAge() {
        return customerRepository.findAverageAge();
    }

    public List<Customer> findYoungCustomers() {
        return StreamSupport.stream(customerRepository
                                .findAll()
                                .spliterator(),
                        false)
                .filter(c -> c.getAge() >= 18 && c.getAge() <= 40)
                .collect(Collectors.toList());
    }
}