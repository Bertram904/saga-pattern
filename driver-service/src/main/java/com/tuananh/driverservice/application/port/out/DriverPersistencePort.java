package com.tuananh.driverservice.application.port.out;

import com.tuananh.driverservice.application.port.in.result.DriverResult;

import java.util.Optional;
import java.util.UUID;

/** Persists driver aggregate; first-available lookup uses pessimistic lock in the adapter. */
public interface DriverPersistencePort {

    /** Returns one AVAILABLE driver row locked for update, or empty if none. */
    Optional<DriverResult> lockFirstAvailableDriver();

    Optional<DriverResult> findById(UUID driverId);

    void save(DriverResult driver);
}
