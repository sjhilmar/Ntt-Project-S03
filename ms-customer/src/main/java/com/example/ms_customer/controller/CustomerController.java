package com.example.ms_customer.controller;

import com.example.ms_customer.model.Customer;
import com.example.ms_customer.service.ICustomerService;
import com.example.ms_customer.util.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Gestión de clientes bancarios")
public class CustomerController {

    private final ICustomerService service;

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping
    public Flux<Customer> getAllCustomers(){
        log.info("Obteniendo todos los clientes");
        return service.getAllCustomer();
    }

    @Operation(summary = "Obtener cliente por Id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getCustomerById(@PathVariable String id){
        log.info("Obteniendo cliente por Id: {}",id);
        return service.getCustomerById(id)
                .map(ResponseEntity::ok)
                .doOnError(e->log.error("Error al obtener el cliente con id: " + id, e))
                .onErrorResume(CustomException.class, e -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Customer>build()));
    }

    @Operation(summary = "Obtener cliente por Número de documento")
    @GetMapping("/documentNumber/{documentNumber}")
    public Mono<ResponseEntity<Customer>> getCustomerByDocumentNumber(@PathVariable String documentNumber){
        log.info("Obteniendo cliente por Número de documento: {}",documentNumber);
        return service.getCustomerByDocumentNumber(documentNumber)
                .map(ResponseEntity::ok)
                .doOnError(e->log.error("Error al obtener el cliente con número de documento: " + documentNumber, e))
                .onErrorResume(CustomException.class, e -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Customer>build()));
    }

    @Operation(summary = "Crear nuevos clientes")
    @PostMapping
    public Mono<? extends ResponseEntity<?>> createCustomer(@Valid @RequestBody Customer customer){
        log.info("Creando un nuevo cliente: {}",customer);
        return service.createCustomer(customer)
                .map(createdCustomer ->ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .doOnError(e-> log.error("Error al crear el cliente\n{}", customer,e))
                .onErrorResume(CustomException.class, e-> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Operation(summary = "Actualizar clientes")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id,@Valid @RequestBody Customer customer){
        log.info("Actualizando cliente por Id: {}",id);
        return service.updateCustomer(id, customer)
                .map(updatedCustomer-> ResponseEntity.status(HttpStatus.OK).body(updatedCustomer))
                .doOnError(e-> log.error("Error al actualizar el cliente con id: " + id, e))
                .onErrorResume(CustomException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Customer>build()));
    }

    @Operation(summary = "Eliminar clientes")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomerById(@PathVariable String id){
        log.info("Eliminando cliente por Id: {}",id);
        return service.deleteCustomerById(id);
    }

}
