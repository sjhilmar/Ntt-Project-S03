package com.example.ms_bootcoin.mapper;

import com.example.ms_bootcoin.dto.UserBootCoinDTO;
import com.example.ms_bootcoin.model.UserBootCoin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserBootCoinDTO toUserBootCoinDTO (UserBootCoin userBootCoin);
    UserBootCoin toUserBootCoin (UserBootCoinDTO userBootCoinDTO);




}
