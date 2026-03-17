package com.tuananh.paymentservice.application.port.out;

import com.tuananh.shared.events.DriverAssignedPayload;

import java.math.BigDecimal;

public interface PaymentOutboxPort {

    void enqueuePaymentAuthorized(DriverAssignedPayload context, BigDecimal reservedAmount);

    void enqueuePaymentFailed(DriverAssignedPayload context, String reason);
}
