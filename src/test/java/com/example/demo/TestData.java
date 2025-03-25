package com.example.demo;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Entites.Customer;

public final class TestData {
    private TestData(){

    }
    public static Customer createCustomer1st() {
        return new Customer(null, "Customer", "1st", 30, "City 1");
    }

    public static CustomerDTO createCustomerDTO1st() {
        return new CustomerDTO(null, "Customer", "1st", 30, "City 1");

    }
    public static Customer createCustomer2nd() {
        return new Customer(null, "Customer", "2nd", 50, "City 2");
    }

    public static CustomerDTO createCustomerDTO2nd() {
        return new CustomerDTO(null, "Customer", "2nd", 50, "City 2");

    }
}
