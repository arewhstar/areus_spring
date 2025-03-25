package com.example.demo.controllers;


import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Entites.Customer;
import com.example.demo.Services.CustomerService;
import com.example.demo.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTests {

    private CustomerService customerService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public CustomerControllerIntegrationTests(MockMvc mockMvc, CustomerService customerService) {
        this.mockMvc = mockMvc;
        this.customerService = customerService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testNoAuthenticationReturnsHttpStatus401() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testThatCreateCustomerSuccessfullyReturnsHttp201Created() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        testCustomer1st.setId(null);
        String customerJson = objectMapper.writeValueAsString(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateCustomerSuccessfullyReturnsSavedCustomer() throws Exception {
        CustomerDTO testCustomer1st = TestData.createCustomerDTO1st();
        testCustomer1st.setId(null);
        String customerJson = objectMapper.writeValueAsString(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstname").value("Customer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastname").value("1st")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(30)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value("City 1")
        );
    }

    @Test
    public void testThatListCustomersReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListCustomersReturnsListOfCustomers() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        customerService.save(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstname").value("Customer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastname").value("1st")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(30)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].address").value("City 1")
        );
    }

    @Test
    public void testThatGetCustomerReturnsHttpStatus200WhenCustomerExist() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        customerService.save(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetCustomerReturnsCustomerWhenCustomerExist() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        customerService.save(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstname").value("Customer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastname").value("1st")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(30)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value("City 1")
        );
    }

    @Test
    public void testThatGetCustomerReturnsHttpStatus404WhenNoCustomerExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateCustomerReturnsHttpStatus404WhenNoCustomerExists() throws Exception {
        CustomerDTO testCustomer1st = TestData.createCustomerDTO1st();
        String customerJson = objectMapper.writeValueAsString(testCustomer1st);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateCustomerReturnsHttpStatus4200WhenCustomerExists() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        Customer savedCustomer = customerService.save(testCustomer1st);

        CustomerDTO testCustomerDTO1st = TestData.createCustomerDTO1st();
        String customerJson = objectMapper.writeValueAsString(testCustomerDTO1st);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingCustomer() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        Customer savedCustomer = customerService.save(testCustomer1st);

        CustomerDTO testCustomerDTO1st = TestData.createCustomerDTO1st();
        testCustomerDTO1st.setId(savedCustomer.getId());

        String customerJson = objectMapper.writeValueAsString(testCustomerDTO1st);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedCustomer.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstname").value(savedCustomer.getFirstname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastname").value(savedCustomer.getLastname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedCustomer.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(savedCustomer.getAddress())
        );
    }

    @Test
    public void testThatPartialUpdateExistingCustomerReturnsHttpStatus20Ok() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        Customer savedCustomer = customerService.save(testCustomer1st);

        CustomerDTO testCustomerDTO1st = TestData.createCustomerDTO1st();
        testCustomerDTO1st.setFirstname("UPDATED");
        String customerJson = objectMapper.writeValueAsString(testCustomerDTO1st);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/customers/" + savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingCustomerReturnsUpdatedCustomer() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        Customer savedCustomer = customerService.save(testCustomer1st);

        CustomerDTO testCustomerDTO1st = TestData.createCustomerDTO1st();
        testCustomerDTO1st.setFirstname("UPDATED");
        String customerJson = objectMapper.writeValueAsString(testCustomerDTO1st);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/customers/" + savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedCustomer.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstname").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastname").value(testCustomerDTO1st.getLastname())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testCustomerDTO1st.getAge())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.address").value(testCustomerDTO1st.getAddress())
        );
    }

    @Test
    public void testThatDeleteCustomerReturnsHttpStatus204ForNonExistingCustomer() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }



    @Test
    public void testThatDeleteCustomerReturnsHttpStatus204ForExistingCustomer() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        Customer savedCustomer = customerService.save(testCustomer1st);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/" + savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatAverageAgeReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/average-age")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatAverageAgeReturnsDouble() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        customerService.save(testCustomer1st);

        Customer testCustomer2nd = TestData.createCustomer2nd();
        customerService.save(testCustomer2nd);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/average-age")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.content().string("40.0")
        );
    }

    @Test
    public void testThatListCustomersBetween18And40ReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/age-young")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListCustomersBetween18And40ReturnsCustomers() throws Exception {
        Customer testCustomer1st = TestData.createCustomer1st();
        customerService.save(testCustomer1st);

        Customer testCustomer2nd = TestData.createCustomer2nd();
        customerService.save(testCustomer2nd);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/age-young")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstname").value("Customer")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastname").value("1st")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(30)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].address").value("City 1")
        );
    }

    @Test
    public void testThatInvalidCustomerIsPosted() throws Exception {


        CustomerDTO testCustomerDTO2nd = TestData.createCustomerDTO2nd();
        testCustomerDTO2nd.setFirstname("");
        String customerJson = objectMapper.writeValueAsString(testCustomerDTO2nd);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .with(httpBasic("admin","admin123"))
        ).andExpect(
                MockMvcResultMatchers.content().string(containsString("Firstname is required"))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
