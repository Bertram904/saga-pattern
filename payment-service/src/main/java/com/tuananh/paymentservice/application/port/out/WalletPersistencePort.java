package com.tuananh.paymentservice.application.port.out;

import com.tuananh.paymentservice.application.port.in.result.WalletResult;

import java.util.Optional;

public interface WalletPersistencePort {

    Optional<WalletResult> findByPassengerId(String passengerId);

    void save(WalletResult wallet);
}
