package com.example.ms_customer.service.impl;

import com.example.ms_customer.model.Customer;
import com.example.ms_customer.repository.CustomerRepository;
import com.example.ms_customer.service.ICustomerService;
import com.example.ms_customer.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Customer> getAllCustomer() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> getCustomerById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("No se encontró cliente con el id: " + id)));
    }

    @Override
    public Mono<Customer> getCustomerByDocumentNumber(String documentNumber) {
        return repository.findCustomerByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new CustomException("No se encontró cliente con el número de documento: " + documentNumber)));
    }

    @Override
    public Mono<Customer> createCustomer(Customer customer) {
      return repository.findCustomerByDocumentNumber(customer.getDocumentNumber())
                .flatMap(existingCustomer -> Mono.error(new CustomException("El cliente ya existe")).cast(Customer.class))
                .switchIfEmpty(Mono.defer(() -> {
                  customer.setCreatedAt(LocalDateTime.now());
                  return repository.save(customer);
                }));
    }

    @Override
    public Mono<Customer> updateCustomer(String id, Customer customer) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("No se encontró cliente")))
                .flatMap(existingCustomer -> {
                    existingCustomer.setDocumentNumber(customer.getDocumentNumber());
                    existingCustomer.setCompanyName(customer.getCompanyName());
                    existingCustomer.setCustomerName(customer.getCustomerName());
                    existingCustomer.setEmail(customer.getEmail());
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                    existingCustomer.setCustomerType(customer.getCustomerType());
                    existingCustomer.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existingCustomer);
                });
    }

    @Override
    public Mono<Void> deleteCustomerById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("No se encontró cliente")))
                .flatMap(repository::delete);
    }
}
