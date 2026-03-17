package com.tuananh.shared.messaging;

/**
 * Topic names for the ride-hailing saga (choreography + transactional outbox).
 */
public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String RIDE_REQUESTED = "ridehailing.ride.requested";
    public static final String DRIVER_ASSIGNED = "ridehailing.driver.assigned";
    public static final String DRIVER_NOT_FOUND = "ridehailing.driver.not_found";
    public static final String PAYMENT_AUTHORIZED = "ridehailing.payment.authorized";
    public static final String PAYMENT_FAILED = "ridehailing.payment.failed";
    public static final String DRIVER_RELEASE = "ridehailing.driver.release";
}
