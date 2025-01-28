package com.example.ms_customer.service.impl;

import com.example.ms_customer.model.Customer;
import com.example.ms_customer.model.enums.CustomerType;
import com.example.ms_customer.repository.CustomerRepository;
import com.example.ms_customer.util.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this.customerRepository);
        MockitoAnnotations.openMocks(this.customerService);
    }

    @Test
    void getAllCustomer() {
        Customer customer1 = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Customer customer2 = new Customer("2", "456", "Company B", "Jane Doe", "jane@example.com", "0987654321", CustomerType.EMPRESARIAL, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2));
        Flux<Customer> result = customerService.getAllCustomer();
        StepVerifier.create(result)
                .expectNext(customer1)
                .expectNext(customer2)
                .verifyComplete();
    }

    @Test
    void getCustomerById() {
        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.getCustomerById(customer.getId());
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void getCustomerByIdNotFound() {
        Mockito.when(customerRepository.findById("1")).thenReturn(Mono.empty());
        Mono<Customer> result = customerService.getCustomerById("1");
        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void getCustomerByDocumentNumber() {
        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(customerRepository.findCustomerByDocumentNumber(customer.getDocumentNumber())).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.getCustomerByDocumentNumber(customer.getDocumentNumber());
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }
    @Test
    void getCustomerByDocumentNumber_NotFound() {
        Mockito.when(customerRepository.findCustomerByDocumentNumber("123")).thenReturn(Mono.empty());
        Mono<Customer> result = customerService.getCustomerByDocumentNumber("123");
        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void createCustomer() {
        Customer customer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(customerRepository.findCustomerByDocumentNumber("123")).thenReturn(Mono.empty());
        Mockito.when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        Mono<Customer> result = customerService.createCustomer(customer);
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void updateCustomer() {
        Customer existingCustomer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Customer updatedCustomer = new Customer("2", "456", "Company B", "Jane Doe", "jane@example.com", "0987654321", CustomerType.EMPRESARIAL, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(customerRepository.findById(existingCustomer.getId())).thenReturn(Mono.just(existingCustomer));
        Mockito.when(customerRepository.save(existingCustomer)).thenReturn(Mono.just(updatedCustomer));
        Mono<Customer> result = customerService.updateCustomer(existingCustomer.getId(), updatedCustomer);
        StepVerifier.create(result)
                .expectNext(updatedCustomer)
                .verifyComplete();
    }

    @Test
    void deleteCustomerById() {
        Customer existingCustomer = new Customer("1", "123", "Company A", "John Doe", "john@example.com", "1234567890", CustomerType.PERSONAL, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(customerRepository.findById(existingCustomer.getId())).thenReturn(Mono.just(existingCustomer));
        Mockito.when(customerRepository.delete(existingCustomer)).thenReturn(Mono.empty());
        Mono<Void> result = customerService.deleteCustomerById(existingCustomer.getId());
        StepVerifier.create(result)
                .verifyComplete();
    }
}