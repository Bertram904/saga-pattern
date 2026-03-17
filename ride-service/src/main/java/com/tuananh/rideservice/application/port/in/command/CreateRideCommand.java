package com.tuananh.rideservice.application.port.in.command;

import java.math.BigDecimal;

/**
 * Inbound command for booking — independent of transport (REST, messaging, etc.).
 */
public record CreateRideCommand(
        String passengerId,
        String pickupLocation,
        String dropOffLocation,
        BigDecimal estimatedAmount,
        String idempotencyKey) {}
