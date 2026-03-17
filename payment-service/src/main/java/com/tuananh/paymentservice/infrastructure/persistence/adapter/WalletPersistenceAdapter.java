package com.tuananh.paymentservice.infrastructure.persistence.adapter;

import com.tuananh.paymentservice.application.port.in.result.WalletResult;
import com.tuananh.paymentservice.application.port.out.WalletPersistencePort;
import com.tuananh.paymentservice.infrastructure.persistence.entity.WalletEntity;
import com.tuananh.paymentservice.infrastructure.persistence.mapper.WalletPersistenceMapper;
import com.tuananh.paymentservice.infrastructure.persistence.repository.WalletJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletPersistenceAdapter implements WalletPersistencePort {

    WalletJpaRepository walletJpaRepository;
    WalletPersistenceMapper walletPersistenceMapper;

    @Override
    public Optional<WalletResult> findByPassengerId(String passengerId) {
        return walletJpaRepository.findById(passengerId).map(walletPersistenceMapper::toResult);
    }

    @Override
    public void save(WalletResult wallet) {
        WalletEntity entity = walletJpaRepository
                .findById(wallet.passengerId())
                .orElseThrow();
        walletPersistenceMapper.copyToEntity(wallet, entity);
        walletJpaRepository.save(entity);
    }
}
