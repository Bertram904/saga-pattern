package com.tuananh.paymentservice.bootstrap;

import com.tuananh.paymentservice.infrastructure.persistence.entity.WalletEntity;
import com.tuananh.paymentservice.infrastructure.persistence.repository.WalletJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Demo wallets for thesis demo (p1 = sufficient balance, poor = insufficient). */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletDataInitializer implements ApplicationRunner {

    WalletJpaRepository walletJpaRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (walletJpaRepository.count() > 0) {
            return;
        }
        walletJpaRepository.save(WalletEntity.builder()
                .passengerId("p1")
                .balance(new BigDecimal("500000.00"))
                .build());
        walletJpaRepository.save(WalletEntity.builder()
                .passengerId("poor")
                .balance(new BigDecimal("100.00"))
                .build());
    }
}
