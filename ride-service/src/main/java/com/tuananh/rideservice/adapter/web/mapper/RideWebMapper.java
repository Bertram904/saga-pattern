package com.tuananh.rideservice.adapter.web.mapper;

import com.tuananh.rideservice.adapter.web.dto.request.CreateRideRequest;
import com.tuananh.rideservice.adapter.web.dto.response.RideResponse;
import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RideWebMapper {

    @Mapping(target = "passengerId", source = "request.passengerId")
    @Mapping(target = "pickupLocation", source = "request.pickupLocation")
    @Mapping(target = "dropOffLocation", source = "request.dropOffLocation")
    @Mapping(target = "estimatedAmount", source = "request.estimatedAmount")
    @Mapping(target = "idempotencyKey", source = "idempotencyKey")
    CreateRideCommand toCommand(CreateRideRequest request, String idempotencyKey);

    RideResponse toResponse(RideResult result);
}
