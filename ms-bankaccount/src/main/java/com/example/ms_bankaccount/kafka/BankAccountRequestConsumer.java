package com.example.ms_bankaccount.kafka;

import com.example.ms_bankaccount.model.BankAccount;
import com.example.ms_bankaccount.model.BankAccountDTO;
import com.example.ms_bankaccount.repository.BankAccountRepository;
import com.example.ms_bankaccount.service.IBankAccountService;
import com.example.ms_bankaccount.util.CustomException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@AllArgsConstructor
public class BankAccountRequestConsumer {

    private static final String RESPONSE_TOPIC = "bank-account-response-topic";
    private final IBankAccountService bankAccountService;
    private final BankAccountRepository bankAccountRepository;
    private final KafkaTemplate<String, BankAccountDTO> kafkaTemplate;

    @KafkaListener(topics = "bank-account-request-topic", groupId = "ms-bankaccount-group")
    public void consumeBankAccountRequest(BankAccountDTO bankAccountDto) {
        if (bankAccountDto != null) {
            log.info("Consumiendo mensaje de la cola bank-account-request-topic, bankAccountId: {}", bankAccountDto);
            Mono<BankAccount> bankAccount = bankAccountRepository.findById(bankAccountDto.getId());
            bankAccount.defaultIfEmpty(new BankAccount()) // Proporciona un valor predeterminado no nulo
                    .map(bankAccount1 -> {
                        if (bankAccount1.getId() != null) {
                            log.info("Cuenta bancaria encontrada: {}", bankAccount1);
                            bankAccountDto.setAccountNumber(bankAccount1.getAccountNumber());
                            bankAccountDto.setBalance(bankAccount1.getBalance());
                            bankAccountDto.setProductType(bankAccount1.getProductType());

                            log.info("Enviando mensaje a la cola bank-account-response-topic, bankAccount:\n{}", bankAccountDto);

                            CompletableFuture<SendResult<String, BankAccountDTO>> future = kafkaTemplate.send(RESPONSE_TOPIC, bankAccountDto);
                            return future.whenCompleteAsync((result, ex) -> {
                                if (ex == null) log.info("Mensaje enviado con éxito: {}", result);
                                else log.error("Error al enviar mensaje: {}", ex.getMessage());
                            });
                        } else {
                            log.error("Consumidor: No se encontró la cuenta bancaria con el id: {}", bankAccountDto.getId());
                        }
                        return bankAccount1;
                    }).subscribe();
        }
    }
}
