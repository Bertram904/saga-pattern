package com.tuananh.driverservice.application.service;

import com.tuananh.driverservice.application.port.in.DriverSagaPort;
import com.tuananh.driverservice.application.port.in.result.DriverResult;
import com.tuananh.driverservice.application.port.out.DriverOutboxPort;
import com.tuananh.driverservice.application.port.out.DriverPersistencePort;
import com.tuananh.shared.events.ReleaseDriverPayload;
import com.tuananh.shared.events.RideRequestedPayload;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Assigns a driver after {@code RIDE_REQUESTED}, or publishes {@code DRIVER_NOT_FOUND}.
 * Publishes outbox rows in the same transaction as driver row updates (transactional messaging).
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverApplicationService implements DriverSagaPort {

    DriverPersistencePort driverPersistencePort;
    DriverOutboxPort driverOutboxPort;

    @Override
    @Transactional
    public void onRideRequested(RideRequestedPayload payload) {
        Optional<DriverResult> candidate = driverPersistencePort.lockFirstAvailableDriver();
        if (candidate.isEmpty()) {
            driverOutboxPort.enqueueDriverNotFound(payload.rideId());
            log.info("No driver for ride {}", payload.rideId());
            return;
        }
        DriverResult assigned = candidate.get().assignToRide(payload.rideId());
        driverPersistencePort.save(assigned);
        driverOutboxPort.enqueueDriverAssigned(payload, assigned.id());
        log.info("Assigned driver {} to ride {}", assigned.id(), payload.rideId());
    }

    @Override
    @Transactional
    public void onReleaseDriver(ReleaseDriverPayload payload) {
        UUID driverId = payload.driverId();
        DriverResult driver =
                driverPersistencePort.findById(driverId).orElse(null);
        if (driver == null) {
            log.warn("ReleaseDriver: driver {} not found", driverId);
            return;
        }
        driverPersistencePort.save(driver.released());
        log.info("Released driver {} ({})", driverId, payload.reason());
    }
}
