package com.example.ms_customer.controller;

import com.example.ms_customer.dto.CustomerSummary;

import com.example.ms_customer.service.ICustomerProductsReport;
import com.example.ms_customer.util.CustomException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/customers/reports")
public class CustomerProductsReportController {

    private final ICustomerProductsReport service;

    @GetMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerSummary>> getProductByCustomerId(@PathVariable String customerId){

        log.info("Obteniendo Productos por Id del CLiente: {}",customerId);

        return service.getProductsByCustomerId(customerId)
                .map(ResponseEntity::ok)
                .doOnError(e->log.error("Error al obtener Los productos con id del cliente: " + customerId, e))
                .onErrorResume(CustomException.class, e -> Mono.just(ResponseEntity.notFound().build()));
    }

}
