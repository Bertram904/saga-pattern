package com.tuananh.rideservice.application.port.out;

import com.tuananh.rideservice.application.port.in.result.RideResult;

import java.util.UUID;

/**
 * Outbound port for transactional outbox writes (same DB transaction as aggregate).
 */
public interface OutboxMessagePort {

    void enqueueRideRequested(RideResult ride);

    void enqueueReleaseDriver(UUID rideId, UUID driverId, String reason);
}
