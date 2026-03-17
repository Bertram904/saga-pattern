package com.tuananh.paymentservice.application.port.in.result;

import java.math.BigDecimal;

public record WalletResult(String passengerId, BigDecimal balance) {

    public WalletResult debit(BigDecimal amount) {
        return new WalletResult(passengerId, balance.subtract(amount));
    }
}
