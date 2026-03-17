package com.tuananh.shared.messaging;

public final class EventTypes {

    private EventTypes() {}

    public static final String RIDE_REQUESTED = "RIDE_REQUESTED";
    public static final String DRIVER_ASSIGNED = "DRIVER_ASSIGNED";
    public static final String DRIVER_NOT_FOUND = "DRIVER_NOT_FOUND";
    public static final String PAYMENT_AUTHORIZED = "PAYMENT_AUTHORIZED";
    public static final String PAYMENT_FAILED = "PAYMENT_FAILED";
    public static final String RELEASE_DRIVER = "RELEASE_DRIVER";
}
