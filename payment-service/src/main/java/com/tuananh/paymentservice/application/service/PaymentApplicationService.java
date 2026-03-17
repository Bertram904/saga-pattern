package com.tuananh.paymentservice.application.service;

import com.tuananh.paymentservice.application.port.in.PaymentSagaPort;
import com.tuananh.paymentservice.application.port.in.result.WalletResult;
import com.tuananh.paymentservice.application.port.out.PaymentOutboxPort;
import com.tuananh.paymentservice.application.port.out.WalletPersistencePort;
import com.tuananh.shared.events.DriverAssignedPayload;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Debits wallet and emits {@code PAYMENT_AUTHORIZED} or {@code PAYMENT_FAILED} via transactional outbox.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentApplicationService implements PaymentSagaPort {

    WalletPersistencePort walletPersistencePort;
    PaymentOutboxPort paymentOutboxPort;

    @Override
    @Transactional
    public void onDriverAssigned(DriverAssignedPayload payload) {
        WalletResult wallet =
                walletPersistencePort.findByPassengerId(payload.passengerId()).orElse(null);
        if (wallet == null) {
            paymentOutboxPort.enqueuePaymentFailed(payload, "WALLET_NOT_FOUND");
            log.warn("No wallet for passenger {}", payload.passengerId());
            return;
        }
        BigDecimal amount = payload.estimatedAmount();
        if (wallet.balance().compareTo(amount) < 0) {
            paymentOutboxPort.enqueuePaymentFailed(payload, "INSUFFICIENT_FUNDS");
            log.info("Insufficient funds ride={} passenger={}", payload.rideId(), payload.passengerId());
            return;
        }
        walletPersistencePort.save(wallet.debit(amount));
        paymentOutboxPort.enqueuePaymentAuthorized(payload, amount);
        log.info("Payment authorized ride={} amount={}", payload.rideId(), amount);
    }
}
