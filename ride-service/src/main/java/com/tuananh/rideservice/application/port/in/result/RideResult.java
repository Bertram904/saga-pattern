package com.tuananh.rideservice.application.port.in.result;

import com.tuananh.rideservice.domain.model.RideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Read model / aggregate snapshot returned from application layer to adapters.
 */
public record RideResult(
        UUID id,
        String passengerId,
        String pickupLocation,
        String dropOffLocation,
        BigDecimal estimatedAmount,
        RideStatus status,
        String idempotencyKey,
        UUID driverId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public RideResult withDriverId(UUID newDriverId) {
        return new RideResult(
                id,
                passengerId,
                pickupLocation,
                dropOffLocation,
                estimatedAmount,
                status,
                idempotencyKey,
                newDriverId,
                createdAt,
                updatedAt);
    }

    public RideResult withStatus(RideStatus newStatus) {
        return new RideResult(
                id,
                passengerId,
                pickupLocation,
                dropOffLocation,
                estimatedAmount,
                newStatus,
                idempotencyKey,
                driverId,
                createdAt,
                updatedAt);
    }

    public RideResult withDriverAndStatus(UUID newDriverId, RideStatus newStatus) {
        return new RideResult(
                id,
                passengerId,
                pickupLocation,
                dropOffLocation,
                estimatedAmount,
                newStatus,
                idempotencyKey,
                newDriverId,
                createdAt,
                updatedAt);
    }
}
