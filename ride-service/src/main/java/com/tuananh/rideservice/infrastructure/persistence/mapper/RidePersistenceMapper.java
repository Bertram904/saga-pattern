package com.tuananh.rideservice.infrastructure.persistence.mapper;

import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import com.tuananh.rideservice.infrastructure.persistence.entity.RideEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RidePersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "REQUESTED")
    @Mapping(target = "driverId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RideEntity toNewEntity(CreateRideCommand command);

    RideResult toResult(RideEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void copyToEntity(RideResult ride, @MappingTarget RideEntity entity);
}
