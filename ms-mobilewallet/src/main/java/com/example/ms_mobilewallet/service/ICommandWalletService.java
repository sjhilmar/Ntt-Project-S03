package com.example.ms_mobilewallet.service;

import com.example.ms_mobilewallet.dto.WalletDTO;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

@Service
public interface ICommandWalletService {
    Single<?> createWallet(WalletDTO walletDTO);
    Single<?> updateWallet(String id, WalletDTO walletDTO);
}
