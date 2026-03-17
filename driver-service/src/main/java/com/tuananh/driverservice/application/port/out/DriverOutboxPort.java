package com.tuananh.driverservice.application.port.out;

import com.tuananh.shared.events.RideRequestedPayload;

import java.util.UUID;

/** Transactional outbox writes (same DB transaction as driver state changes). */
public interface DriverOutboxPort {

    void enqueueDriverAssigned(RideRequestedPayload rideContext, UUID driverId);

    void enqueueDriverNotFound(UUID rideId);
}
