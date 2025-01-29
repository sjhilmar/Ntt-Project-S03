package com.example.ms_bootcoin.mapper;

import com.example.ms_bootcoin.dto.CurrencyRateDTO;
import com.example.ms_bootcoin.model.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {
    CurrencyRateMapper INSTANCE = Mappers.getMapper(CurrencyRateMapper.class);

    CurrencyRateDTO toCurrencyRateDTO (CurrencyRate currencyRate);
    CurrencyRate toCurrencyRate (CurrencyRateDTO currencyRateDTO);

}
