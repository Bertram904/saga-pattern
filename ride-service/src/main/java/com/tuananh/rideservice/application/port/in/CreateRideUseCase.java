package com.tuananh.rideservice.application.port.in;

import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;

public interface CreateRideUseCase {
    RideResult createRide(CreateRideCommand command);
}
