package com.example.ms_customer.controller;

import com.example.ms_customer.model.Customer;
import com.example.ms_customer.model.enums.CustomerType;
import com.example.ms_customer.service.ICustomerService;
import com.example.ms_customer.util.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private ICustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this.customerService);
        MockitoAnnotations.openMocks(this.customerController);
        customer1 = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        customer2 = new Customer("2", "456", "Company B", "Jane Doe", "jane@example.com", "0987654321", CustomerType.EMPRESARIAL, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void getAllCustomers() {
        Mockito.when(customerService.getAllCustomer()).thenReturn(Flux.just(customer1, customer2));
        Flux<Customer> result = customerController.getAllCustomers();
        StepVerifier.create(result)
                .expectNext(customer1)
                .expectNext(customer2)
                .verifyComplete();
    }

    @Test
    void getCustomerById() {
        Mockito.when(customerService.getCustomerById("1")).thenReturn(Mono.just(customer1));
        Mono<ResponseEntity<Customer>> result = customerController.getCustomerById("1");
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(customer1))
                .verifyComplete();
    }
    @Test
    void getCustomerById_NotFound() {
        Mockito.when(customerService.getCustomerById("1")).thenReturn(Mono.error(new CustomException("Customer not found")));
        Mono<ResponseEntity<Customer>> result = customerController.getCustomerById("1");
        StepVerifier.create(result)
                .expectNext(ResponseEntity.notFound().build())
                .verifyComplete();
    }

    @Test
    void getCustomerByDocumentNumber() {
        Mockito.when(customerService.getCustomerByDocumentNumber("123")).thenReturn(Mono.just(customer1));
        Mono<ResponseEntity<Customer>> result = customerController.getCustomerByDocumentNumber("123");
        StepVerifier.create(result)
                .expectNext(ResponseEntity.ok(customer1))
                .verifyComplete();
    }

    @Test
    void createCustomer() {
        Mockito.when(customerService.createCustomer(any(Customer.class))).thenReturn(Mono.just(customer1));
        Mono<ResponseEntity<?>> result = (Mono<ResponseEntity<?>>) customerController.createCustomer(customer1);
        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.CREATED).body(customer1))
                .verifyComplete();
    }


    @Test
    void updateCustomer() {
        Mockito.when(customerService.updateCustomer(anyString(), any(Customer.class))).thenReturn(Mono.just(customer1));
        Mono<ResponseEntity<Customer>> result = customerController.updateCustomer("1", customer1);
        StepVerifier.create(result)
                .expectNext(ResponseEntity.status(HttpStatus.OK).body(customer1))
                .verifyComplete();
    }

    @Test
    void deleteCustomerById() {
        Mockito.when(customerService.deleteCustomerById("1")).thenReturn(Mono.empty());
        Mono<Void> result = customerController.deleteCustomerById("1");
        StepVerifier.create(result)
                .verifyComplete();
    }
}