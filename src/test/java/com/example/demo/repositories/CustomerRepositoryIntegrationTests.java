package com.example.demo.repositories;

import com.example.demo.TestData;
import com.example.demo.Entites.Customer;
import com.example.demo.Repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositoryIntegrationTests {
    private CustomerRepository testRepository;

    @Autowired
    public CustomerRepositoryIntegrationTests(CustomerRepository testRepository) {
        this.testRepository = testRepository;
    }



    @Test
    public void testCreateCustomer() {
        Customer Customer = TestData.createCustomer1st();
        testRepository.save(Customer);
        Optional<Customer> result = testRepository.findById(Customer.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(Customer);
    }

    @Test
    public void testCreateCustomers() {
        Customer Customer1st = TestData.createCustomer1st();
        testRepository.save(Customer1st);
        Customer Customer2nd = TestData.createCustomer2nd();
        testRepository.save(Customer2nd);

        Iterable<Customer> result = testRepository.findAll();
        assertThat(result)
                .hasSize(2).
                containsExactly(Customer1st, Customer2nd);
    }

    @Test
    public void testUpdateCustomer() {
        Customer Customer1st = TestData.createCustomer1st();
        testRepository.save(Customer1st);
        Customer1st.setFirstname("UPDATED");
        testRepository.save(Customer1st);
        Optional<Customer> result = testRepository.findById(Customer1st.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(Customer1st);
    }

    @Test
    public void testDeleteCustomer() {
        Customer Customer1st = TestData.createCustomer1st();
        testRepository.save(Customer1st);
        testRepository.deleteById(Customer1st.getId());
        Optional<Customer> result = testRepository.findById(Customer1st.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetAverageAge() {
        Customer testCustomer1st = TestData.createCustomer1st();
        testRepository.save(testCustomer1st);
        Customer testCustomer2nd = TestData.createCustomer2nd();
        testRepository.save(testCustomer2nd);

        float customersNum = testRepository.count();
        Double result = testRepository.findAverageAge();
        assertThat(result).isNotNull().isEqualTo((testCustomer1st.getAge() + testCustomer2nd.getAge()) / customersNum);
    }


}
