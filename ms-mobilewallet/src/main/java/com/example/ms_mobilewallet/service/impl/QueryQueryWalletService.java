package com.example.ms_mobilewallet.service.impl;

import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.repository.QueryWalletRepository;
import com.example.ms_mobilewallet.service.IQueryWalletService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

@Service
public class QueryQueryWalletService implements IQueryWalletService {

    private final QueryWalletRepository queryWalletRepository;


    public QueryQueryWalletService(QueryWalletRepository queryWalletRepository) {
        this.queryWalletRepository = queryWalletRepository;

    }

    @Override
    public Flowable<Wallet> getAllWallets() {
        return Flowable.fromPublisher(queryWalletRepository.findAll());
    }

    @Override
    public Single<Wallet> getWalletById(String id) {
        return Single.fromPublisher(queryWalletRepository.findById(id));
    }

    @Override
    public Single<Wallet> getWalletByDocumentNumber(String documentNumber) {
        return queryWalletRepository.findWalletByDocumentNumber(documentNumber);
    }





}