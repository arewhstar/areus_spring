package com.example.demo.Controllers;
import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Entites.Customer;
import com.example.demo.Mappers.Mapper;
import com.example.demo.Services.Impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class CustomerController {
    private CustomerServiceImpl customerServiceImpl;

    private Mapper<Customer, CustomerDTO> customerMapper;


    public CustomerController(CustomerServiceImpl customerServiceImpl, Mapper<Customer, CustomerDTO> customerMapper) {
        this.customerServiceImpl = customerServiceImpl;
        this.customerMapper = customerMapper;
    }

    @PostMapping(path = "/customers")
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerDTO customer, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        Customer customerEntity = customerMapper.mapFrom(customer);
        Customer savedCustomerEntity = customerServiceImpl.save(customerEntity);
        return new ResponseEntity<>(customerMapper.mapTo(savedCustomerEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/customers")
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerServiceImpl.findAll();
        return customers.stream()
                .map(customerMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id) {
        Optional<Customer> foundCustomer = customerServiceImpl.findOne(id);
        return foundCustomer.map(customerEntity -> {
            CustomerDTO customerDto = customerMapper.mapTo(customerEntity);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/customers/{id}")
    public ResponseEntity fullUpdateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerDTO customerDTO,BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if(!customerServiceImpl.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        customerDTO.setId(id);
        Customer customer = customerMapper.mapFrom(customerDTO);
        Customer savedCustomer = customerServiceImpl.save(customer);
        return new ResponseEntity<>(
                customerMapper.mapTo(savedCustomer),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/customers/{id}")
    public ResponseEntity partialUpdate(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerDTO customerDto,BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if(!customerServiceImpl.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customerEntity = customerMapper.mapFrom(customerDto);
        Customer updatedCustomer = customerServiceImpl.partialUpdate(id, customerEntity);
        return new ResponseEntity<>(
                customerMapper.mapTo(updatedCustomer),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/customers/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Long id) {
        customerServiceImpl.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/customers/average-age")
    public ResponseEntity<Double> getAverageAge() {
        Double avgAge = customerServiceImpl.getAverageAge();
        return new ResponseEntity<Double>(
                avgAge,
                HttpStatus.OK);
    }

    @GetMapping("/customers/age-young")
    public List<CustomerDTO> getYoungCustomers() {
        List<Customer> customers = customerServiceImpl.findYoungCustomers();
        return customers.stream()
                .map(customerMapper::mapTo)
                .collect(Collectors.toList());
    }
}