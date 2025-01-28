package com.example.ms_mobilewallet.kafka;

import com.example.ms_mobilewallet.dto.BankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@AllArgsConstructor
public class BankAccountRequestProducer {

    private final static String TOPIC= "bank-account-request-topic";
    private final KafkaTemplate<String, BankAccountDTO> kafkaTemplate;

    public void sendBankAccountRequest(BankAccountDTO bankAccount) {
        CompletableFuture< SendResult<String, BankAccountDTO>> future = kafkaTemplate.send(TOPIC, bankAccount);
        future.whenCompleteAsync((result, ex) -> {
           if (ex != null) log.error("Error al enviar el mensaje: {}", ex.getMessage());
           else log.info("Mensaje enviado con éxito:\n offset: {}\nBankAccount: {}", result.getRecordMetadata().offset(),bankAccount);
        });

    }
}
