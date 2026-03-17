package com.tuananh.rideservice.adapter.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * HTTP contract for booking — validated at the edge; mapped to {@link com.tuananh.rideservice.application.port.in.command.CreateRideCommand}.
 */
@Data
public class CreateRideRequest {

    @NotBlank(message = "passengerId is required")
    @Size(max = 128, message = "passengerId must be at most 128 characters")
    private String passengerId;

    @NotBlank(message = "pickupLocation is required")
    @Size(max = 512, message = "pickupLocation must be at most 512 characters")
    private String pickupLocation;

    @NotBlank(message = "dropOffLocation is required")
    @Size(max = 512, message = "dropOffLocation must be at most 512 characters")
    private String dropOffLocation;

    @NotNull(message = "estimatedAmount is required")
    @DecimalMin(value = "0.01", message = "estimatedAmount must be at least 0.01")
    private BigDecimal estimatedAmount;
}
