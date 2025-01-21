package com.example.ms_bankcredit.service.impl;

import com.example.ms_bankcredit.model.Credit;
import com.example.ms_bankcredit.model.enums.CustomerType;
import com.example.ms_bankcredit.model.enums.ProductType;
import com.example.ms_bankcredit.repository.CreditRepository;
import com.example.ms_bankcredit.service.ICreditService;
import com.example.ms_bankcredit.service.ICustomerService;
import com.example.ms_bankcredit.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final CreditRepository creditRepository;

    private final ICustomerService customerService;

    @Override
    public Flux<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    @Override
    public Mono<Credit> getCreditById(String id) {
        return creditRepository.findById(id);
    }

    @Override
    public Flux<Credit> getCreditByHolderById(String id) {
        return creditRepository.findByHolderId(id);
    }

    @Override
    public Mono<Credit> createCredit(Credit credit) {
        return customerService.getCustomerByDocumentNumber(credit.getHolder().getDocumentNumber())
                .flatMap(customer -> {
                 credit.setHolder(customer);

                 return creditRepository.findByHolderId(credit.getHolder().getId())
                         .filter(existingCredit -> existingCredit.getBalance().compareTo(BigDecimal.ZERO)> 0
                         && existingCredit.getEndDate().isBefore(LocalDate.now()))
                         .hasElements()
                         .flatMap(hasDebt  ->{
                           if (hasDebt){
                               return Mono.error(new CustomException("El cliente tiene deudas vencidos, no puede adquirir un nuevo credito"));
                           }

                         return creditRepository.findByHolderId(credit.getHolder().getId())
                                 .collectList()
                                 .flatMap( existingCredit ->{
                                    if (credit.getHolder().getCustomerType() == CustomerType.PERSONAL || credit.getHolder().getCustomerType() == CustomerType.PERSONAL_VIP){

                                        long personalCreditCount = existingCredit.stream().filter(personalCredit -> credit.getProductType() == ProductType.CREDITO_PERSONAL).count();

                                        if (credit.getProductType() == ProductType.CREDITO_PERSONAL && personalCreditCount>0){
                                            return Mono.error(new CustomException("El cliente personal solo puede tener un crédito personal"));
                                        }
                                        if (credit.getProductType() == ProductType.TARJETA_CREDITO){
                                            return Mono.error(new CustomException("Solo se registran creditos personales o empresariales"));
                                        }
                                        if (credit.getProductType() == ProductType.CREDITO_EMPRESARIAL){
                                            return Mono.error(new CustomException("El cliente personal no puede tener un crédito empresarial"));
                                        }
                                        credit.setCreatedAt(LocalDateTime.now());
                                        credit.setBalance(credit.getCreditAmount());
                                        return creditRepository.save(credit);

                                    }else if (credit.getHolder().getCustomerType()== CustomerType.EMPRESARIAL || credit.getHolder().getCustomerType()== CustomerType.EMPRESARIAL_MYPE){
                                        if (credit.getProductType() == ProductType.CREDITO_PERSONAL){
                                            return Mono.error(new CustomException("El Cliente empresarial no puede tener un credito personal"));
                                        }
                                        if (credit.getHolder().getDocumentNumber() == null){
                                            return Mono.error(new CustomException("El cliente empresarial debe tener al menos un titular"));
                                        }
                                        credit.setCreatedAt(LocalDateTime.now());
                                        credit.setBalance(credit.getCreditAmount());
                                        return creditRepository.save(credit);
                                    }else{
                                        return Mono.error(new CustomException("No existe el tipo de cliente"));
                                    }

                                 });

                         });
                });
    }

    @Override
    public Mono<Credit> updateCredit(String id, Credit credit) {
        return customerService.getCustomerByDocumentNumber(credit.getHolder().getDocumentNumber())
                .flatMap(customer -> {
                    credit.setHolder(customer);
                    return creditRepository.findById(id)
                            .switchIfEmpty(Mono.error(new CustomException("No se encontró el crédito con Id: " + id)))
                            .flatMap( existingCredit ->{
                               existingCredit.setHolder(customer);
                               existingCredit.setCreditAmount(credit.getCreditAmount());
                               existingCredit.setInteresRate(credit.getInteresRate());
                               existingCredit.setLoanTerm(credit.getLoanTerm());
                               existingCredit.setStartDate(credit.getStartDate());
                               existingCredit.setEndDate(credit.getEndDate());
                               existingCredit.setProductType(credit.getProductType());
                               existingCredit.setMonthPayment(credit.getMonthPayment());
                               existingCredit.setUpdatedAt(LocalDateTime.now());
                               return creditRepository.save(existingCredit);
                            });
                });
    }

    @Override
    public Mono<Credit> updateCreditBalance(String id, BigDecimal newBalance) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Credito no encontrado")))
                .flatMap(existingCredit ->{
                    existingCredit.setBalance(newBalance);
                    return creditRepository.save(existingCredit);
                });
    }

    @Override
    public Mono<Void> deleteCreditById(String id) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Credito no encontrado")))
                .flatMap(existingCredit -> creditRepository.deleteById(id));
    }
}
