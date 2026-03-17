package com.tuananh.paymentservice.application.port.in;

import com.tuananh.shared.events.DriverAssignedPayload;

/** Inbound: authorize payment after driver assigned (next saga step). */
public interface PaymentSagaPort {

    void onDriverAssigned(DriverAssignedPayload payload);
}
