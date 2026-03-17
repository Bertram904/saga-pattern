package com.tuananh.driverservice.infrastructure.persistence.mapper;

import com.tuananh.driverservice.application.port.in.result.DriverResult;
import com.tuananh.driverservice.infrastructure.persistence.entity.DriverEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverPersistenceMapper {

    DriverResult toResult(DriverEntity entity);

    void copyToEntity(DriverResult driver, @MappingTarget DriverEntity entity);
}
