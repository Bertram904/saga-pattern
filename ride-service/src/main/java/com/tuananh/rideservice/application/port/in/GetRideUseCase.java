package com.tuananh.rideservice.application.port.in;

import com.tuananh.rideservice.application.port.in.result.RideResult;

import java.util.UUID;

public interface GetRideUseCase {
    RideResult getRide(UUID rideId);
}
