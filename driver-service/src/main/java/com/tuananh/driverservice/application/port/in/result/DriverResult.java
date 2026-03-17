package com.tuananh.driverservice.application.port.in.result;

import com.tuananh.driverservice.domain.model.DriverStatus;

import java.util.UUID;

/** Immutable driver snapshot for application logic (no JPA in this layer). */
public record DriverResult(UUID id, String name, DriverStatus status, UUID assignedRideId) {

    public DriverResult assignToRide(UUID rideId) {
        return new DriverResult(id, name, DriverStatus.BUSY, rideId);
    }

    public DriverResult released() {
        return new DriverResult(id, name, DriverStatus.AVAILABLE, null);
    }
}
