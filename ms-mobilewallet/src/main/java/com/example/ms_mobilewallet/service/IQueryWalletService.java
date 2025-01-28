package com.example.ms_mobilewallet.service;

import com.example.ms_mobilewallet.model.Wallet;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IQueryWalletService {

    Flowable<Wallet> getAllWallets();
    Single<Wallet> getWalletById(String id);
    Single<Wallet> getWalletByDocumentNumber(String documentNumber);


}
