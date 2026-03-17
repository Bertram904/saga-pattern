package com.tuananh.rideservice.application.port.out;

import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound persistence port — implemented by infrastructure (JPA).
 */
public interface RidePersistencePort {

    Optional<RideResult> findById(UUID rideId);

    Optional<RideResult> findByIdempotencyKey(String idempotencyKey);

    RideResult saveNew(CreateRideCommand command);

    void update(RideResult ride);
}
