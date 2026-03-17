package com.tuananh.shared.events;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DriverAssignedPayload(
        UUID rideId,
        UUID driverId,
        String passengerId,
        BigDecimal estimatedAmount
) {}
