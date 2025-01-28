package com.example.ms_mobilewallet.mapper;

import com.example.ms_mobilewallet.dto.WalletDTO;
import com.example.ms_mobilewallet.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletDTO toWalletDTO(Wallet wallet);
    Wallet toWallet(WalletDTO walletDTO);
}
