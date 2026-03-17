package com.tuananh.rideservice.application.service;

import com.tuananh.rideservice.application.port.in.CreateRideUseCase;
import com.tuananh.rideservice.application.port.in.GetRideUseCase;
import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import com.tuananh.rideservice.application.port.out.OutboxMessagePort;
import com.tuananh.rideservice.application.port.out.RidePersistencePort;
import com.tuananh.rideservice.domain.exception.RideNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Books a ride: idempotent by key; persists ride + `RIDE_REQUESTED` outbox in one transaction. */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RideBookingApplicationService implements CreateRideUseCase, GetRideUseCase {

    RidePersistencePort ridePersistencePort;
    OutboxMessagePort outboxMessagePort;

    @Override
    @Transactional
    public RideResult createRide(CreateRideCommand command) {
        log.info("Create ride command passenger={} idempotencyKey={}", command.passengerId(), command.idempotencyKey());

        return ridePersistencePort
                .findByIdempotencyKey(command.idempotencyKey())
                .map(existing -> {
                    log.info("Idempotent replay: returning ride {}", existing.id());
                    return existing;
                })
                .orElseGet(() -> {
                    RideResult saved = ridePersistencePort.saveNew(command);
                    outboxMessagePort.enqueueRideRequested(saved);
                    log.info("Persisted ride {} and RIDE_REQUESTED outbox in one transaction.", saved.id());
                    return saved;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public RideResult getRide(UUID rideId) {
        return ridePersistencePort.findById(rideId).orElseThrow(() -> new RideNotFoundException(rideId));
    }
}
