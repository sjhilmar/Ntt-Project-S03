package com.example.ms_mobilewallet.repository;

import com.example.ms_mobilewallet.dto.WalletDTO;
import com.example.ms_mobilewallet.model.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommandWalletRepository extends ReactiveMongoRepository<Wallet,String> {

}
