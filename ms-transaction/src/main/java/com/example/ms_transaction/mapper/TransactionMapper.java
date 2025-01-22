package com.example.ms_transaction.mapper;
import com.example.ms_transaction.model.Transaction;
import com.example.ms_transaction.service.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

//    @Mapping(source = "product.id", target = "productId")
//    @Mapping(source = "targetProduct.id", target = "targetProductId")
    TransactionDTO toTransactionDTO(Transaction transaction);

//    @Mapping(source = "productId", target = "product.id")
//    @Mapping(source = "targetProductId", target = "targetProduct.id")
    Transaction toTransaction(TransactionDTO transactionDTO);

}