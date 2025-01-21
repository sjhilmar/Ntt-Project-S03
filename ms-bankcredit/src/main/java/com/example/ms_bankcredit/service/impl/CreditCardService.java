package com.example.ms_bankcredit.service.impl;

import com.example.ms_bankcredit.model.CreditCard;
import com.example.ms_bankcredit.model.enums.CustomerType;
import com.example.ms_bankcredit.model.enums.ProductType;
import com.example.ms_bankcredit.repository.CreditCardRepository;
import com.example.ms_bankcredit.service.ICreditCardService;
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
public class CreditCardService implements ICreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final ICustomerService customerService;
    private final ICreditService creditService;

    @Override
    public Flux<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public Mono<CreditCard> getCreditCardById(String id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public Flux<CreditCard> getCreditCardByHolderById(String id) {
        return creditCardRepository.findByHolderId(id);
    }

    @Override
    public Mono<CreditCard> createCreditCard(CreditCard creditCard) {
        return customerService.getCustomerByDocumentNumber(creditCard.getHolder().getDocumentNumber())
                .flatMap(customer -> {
                    creditCard.setHolder(customer);
                    return creditService.getCreditByHolderById(creditCard.getHolder().getId())
                            .filter(existingCredit -> existingCredit.getBalance().compareTo(BigDecimal.ZERO)> 0
                            && existingCredit.getEndDate().isBefore(LocalDate.now()))
                            .hasElements()
                            .flatMap(hasDebt -> {
                               if (hasDebt) return Mono.error(new CustomException("El cliente tiene deudas vencidas, no puede adquirir un nuevo credito"));

                            return creditCardRepository.findByHolderId(creditCard.getHolder().getId())
                                    .collectList()
                                    .flatMap( existingCredit ->{
                                        if (creditCard.getHolder().getCustomerType() == CustomerType.PERSONAL || creditCard.getHolder().getCustomerType() == CustomerType.PERSONAL_VIP){

                                            long creditCardCount = existingCredit.stream().filter(personalCredit -> creditCard.getProductType() == ProductType.TARJETA_CREDITO).count();

                                            if (creditCard.getProductType() == ProductType.CREDITO_PERSONAL || creditCard.getProductType()==ProductType.CREDITO_EMPRESARIAL){
                                                return Mono.error(new CustomException("Solo se registran tarjetas de credito"));
                                            }
                                            if (creditCard.getProductType() == ProductType.TARJETA_CREDITO && creditCardCount>0){
                                                return Mono.error(new CustomException("El cliente personal solo puede tener una tarjeta de credito"));
                                            }

                                            creditCard.setCreatedAt(LocalDateTime.now());
                                            creditCard.setBalance(creditCard.getCreditLine());
                                            creditCard.setConsumption(BigDecimal.ZERO);
                                            return creditCardRepository.save(creditCard);

                                        }else if (creditCard.getHolder().getCustomerType()== CustomerType.EMPRESARIAL || creditCard.getHolder().getCustomerType()== CustomerType.EMPRESARIAL_MYPE){
                                            if (creditCard.getProductType() == ProductType.CREDITO_PERSONAL || creditCard.getProductType()==ProductType.CREDITO_EMPRESARIAL){
                                                return Mono.error(new CustomException("Solo se registran tarjetas de credito"));
                                            }
                                            if (creditCard.getHolder().getDocumentNumber() == null){
                                                return Mono.error(new CustomException("El cliente empresarial debe tener al menos un titular"));
                                            }
                                            creditCard.setCreatedAt(LocalDateTime.now());
                                            creditCard.setBalance(creditCard.getCreditLine());
                                            creditCard.setConsumption(BigDecimal.ZERO);
                                            return creditCardRepository.save(creditCard);
                                        }else{
                                            return Mono.error(new CustomException("No existe el tipo de cliente"));
                                        }
                            });
                    });
                });
    }

    @Override
    public Mono<CreditCard> updateCreditCard(String id, CreditCard credit) {
        return customerService.getCustomerByDocumentNumber(credit.getHolder().getDocumentNumber())
                .flatMap(customer -> {
                    credit.setHolder(customer);
                    return creditCardRepository.findById(id)
                            .switchIfEmpty(Mono.error(new CustomException("No se encontró el crédito con Id: " + id)))
                            .flatMap( existingCredit ->{
                                existingCredit.setHolder(customer);
                                existingCredit.setCardNumber(credit.getCardNumber());
                                existingCredit.setCreditLine(credit.getCreditLine());
                                existingCredit.setProductType(credit.getProductType());
                                existingCredit.setUpdatedAt(LocalDateTime.now());
                                return creditCardRepository.save(existingCredit);
                            });
                });
    }

    @Override
    public Mono<CreditCard> updateCreditCardBalance(String id, BigDecimal newBalance) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Tarjeta de Credito no encontrado")))
                .flatMap(existingCredit ->{
                    existingCredit.setBalance(newBalance);
                    return creditCardRepository.save(existingCredit);
                });
    }

    @Override
    public Mono<Void> deleteCreditCardById(String id) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Tarjeta de Credito no encontrado")))
                .flatMap(existingCredit -> creditCardRepository.deleteById(id));
    }
}
