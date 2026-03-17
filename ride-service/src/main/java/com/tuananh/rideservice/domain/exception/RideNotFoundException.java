package com.tuananh.rideservice.domain.exception;

import com.tuananh.rideservice.domain.error.AppError;

import java.util.Map;
import java.util.UUID;

public class RideNotFoundException extends DomainException {

    public RideNotFoundException(UUID rideId) {
        super(
                AppError.RIDE_NOT_FOUND,
                "Ride not found: " + rideId,
                Map.of("rideId", rideId.toString()));
    }
}
