package com.tuananh.rideservice.domain.model;

/**
 * Aggregate lifecycle for ride booking saga.
 */
public enum RideStatus {
    REQUESTED,
    AWAITING_PAYMENT,
    CONFIRMED,
    CANCELLED_NO_DRIVER,
    CANCELLED_PAYMENT,
}
