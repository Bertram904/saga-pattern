package com.tuananh.rideservice.application.service;

import com.tuananh.rideservice.application.port.in.RideSagaPort;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import com.tuananh.rideservice.application.port.out.OutboxMessagePort;
import com.tuananh.rideservice.application.port.out.RidePersistencePort;
import com.tuananh.rideservice.domain.model.RideStatus;
import com.tuananh.shared.events.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** Applies saga events from Kafka; on payment failure enqueues `RELEASE_DRIVER` via outbox (compensation). */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RideStateApplicationService implements RideSagaPort {

    RidePersistencePort ridePersistencePort;
    OutboxMessagePort outboxMessagePort;

    @Override
    public void onDriverAssigned(DriverAssignedPayload payload) {
        RideResult ride = ridePersistencePort.findById(payload.rideId()).orElse(null);
        if (ride == null) {
            log.warn("DriverAssigned ignored: ride {} not found", payload.rideId());
            return;
        }
        if (ride.status() != RideStatus.REQUESTED) {
            log.info("DriverAssigned ignored: ride {} state {}", ride.id(), ride.status());
            return;
        }
        ridePersistencePort.update(ride.withDriverAndStatus(payload.driverId(), RideStatus.AWAITING_PAYMENT));
        log.info("Ride {} awaiting payment (driver {}).", ride.id(), payload.driverId());
    }

    @Override
    public void onDriverNotFound(DriverNotFoundPayload payload) {
        RideResult ride = ridePersistencePort.findById(payload.rideId()).orElse(null);
        if (ride == null) {
            return;
        }
        if (ride.status() != RideStatus.REQUESTED) {
            return;
        }
        ridePersistencePort.update(ride.withStatus(RideStatus.CANCELLED_NO_DRIVER));
        log.info("Ride {} cancelled: no driver ({})", ride.id(), payload.reason());
    }

    @Override
    public void onPaymentAuthorized(PaymentAuthorizedPayload payload) {
        RideResult ride = ridePersistencePort.findById(payload.rideId()).orElse(null);
        if (ride == null) {
            return;
        }
        if (ride.status() == RideStatus.CONFIRMED) {
            return;
        }
        if (ride.status() == RideStatus.CANCELLED_NO_DRIVER || ride.status() == RideStatus.CANCELLED_PAYMENT) {
            return;
        }
        if (ride.status() == RideStatus.REQUESTED || ride.status() == RideStatus.AWAITING_PAYMENT) {
            UUID driverId = ride.driverId() != null ? ride.driverId() : payload.driverId();
            ridePersistencePort.update(ride.withDriverAndStatus(driverId, RideStatus.CONFIRMED));
            log.info("Ride {} confirmed (payment authorized).", payload.rideId());
        }
    }

    @Override
    public void onPaymentFailed(PaymentFailedPayload payload) {
        RideResult ride = ridePersistencePort.findById(payload.rideId()).orElse(null);
        if (ride == null) {
            return;
        }
        if (ride.status() == RideStatus.CONFIRMED) {
            return;
        }
        if (ride.status() == RideStatus.CANCELLED_NO_DRIVER || ride.status() == RideStatus.CANCELLED_PAYMENT) {
            return;
        }
        if (ride.status() == RideStatus.REQUESTED || ride.status() == RideStatus.AWAITING_PAYMENT) {
            UUID driverId = ride.driverId() != null ? ride.driverId() : payload.driverId();
            RideResult cancelled = ride.withDriverAndStatus(driverId, RideStatus.CANCELLED_PAYMENT);
            ridePersistencePort.update(cancelled);

            UUID driverToRelease = payload.driverId() != null ? payload.driverId() : cancelled.driverId();
            if (driverToRelease != null) {
                outboxMessagePort.enqueueReleaseDriver(payload.rideId(), driverToRelease, payload.reason());
            }
            log.info("Ride {} cancelled: payment failed — compensation queued.", payload.rideId());
        }
    }
}
