package com.example.ms_mobilewallet.kafka;

import com.example.ms_mobilewallet.dto.BankAccountDTO;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@Service
@AllArgsConstructor
public class BankAccountResponseConsumer {

    private final ObjectMapper objectMapper;
    private final ConcurrentMap<String, CompletableFuture<BankAccountDTO>> futures = new ConcurrentHashMap<>();

    public CompletableFuture<BankAccountDTO> getFuture(String key) {
        CompletableFuture<BankAccountDTO> future = new CompletableFuture<>();
        futures.put(key, future);
        return future;
    }

    @KafkaListener(topics = "bank-account-response-topic", groupId = "ms-mobilewallet-group")
    public void consumeBankAccountResponse(String message) {
        log.info("Consumiendo topico bank-account-response-topic, retornando cuenta bancaria \n{}", message    );
        BankAccountDTO bankAccountDTO = parseMessage(message);
        if (bankAccountDTO != null) {
            CompletableFuture<BankAccountDTO> future = futures.remove(bankAccountDTO.getId());
            if (future != null) {
                future.complete(bankAccountDTO);
            } else {
                log.error("No future found for bank account ID: {}", bankAccountDTO.getId());
            }
        }

    }

    private BankAccountDTO parseMessage(String message) {
        try {
            return objectMapper.readValue(message, BankAccountDTO.class);
        } catch (Exception e) {
            log.error("Error al parsear el mensaje: {}", e.getMessage());
            return null;
        }
    }

}
