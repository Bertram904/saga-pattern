package com.tuananh.rideservice.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuananh.rideservice.domain.model.RideStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RideResponse {
    UUID id;
    String passengerId;
    String pickupLocation;
    String dropOffLocation;
    BigDecimal estimatedAmount;
    RideStatus status;
    UUID driverId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
