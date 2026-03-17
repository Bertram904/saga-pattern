package com.tuananh.driverservice.application.port.in;

import com.tuananh.shared.events.ReleaseDriverPayload;
import com.tuananh.shared.events.RideRequestedPayload;

/** Inbound saga steps from Kafka (ride requested → assign; compensation → release). */
public interface DriverSagaPort {

    void onRideRequested(RideRequestedPayload payload);

    void onReleaseDriver(ReleaseDriverPayload payload);
}
