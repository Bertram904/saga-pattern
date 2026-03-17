package com.tuananh.rideservice.application.port.in;

import com.tuananh.shared.events.*;

/**
 * Inbound port for choreography saga callbacks (Kafka adapter delegates here).
 */
public interface RideSagaPort {

    void onDriverAssigned(DriverAssignedPayload payload);

    void onDriverNotFound(DriverNotFoundPayload payload);

    void onPaymentAuthorized(PaymentAuthorizedPayload payload);

    void onPaymentFailed(PaymentFailedPayload payload);
}
