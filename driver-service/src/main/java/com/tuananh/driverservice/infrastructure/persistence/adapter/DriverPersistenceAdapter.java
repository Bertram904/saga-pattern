package com.tuananh.driverservice.infrastructure.persistence.adapter;

import com.tuananh.driverservice.application.port.in.result.DriverResult;
import com.tuananh.driverservice.application.port.out.DriverPersistencePort;
import com.tuananh.driverservice.domain.model.DriverStatus;
import com.tuananh.driverservice.infrastructure.persistence.entity.DriverEntity;
import com.tuananh.driverservice.infrastructure.persistence.mapper.DriverPersistenceMapper;
import com.tuananh.driverservice.infrastructure.persistence.repository.DriverJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverPersistenceAdapter implements DriverPersistencePort {

    DriverJpaRepository driverJpaRepository;
    DriverPersistenceMapper driverPersistenceMapper;

    @Override
    public Optional<DriverResult> lockFirstAvailableDriver() {
        List<DriverEntity> candidates =
                driverJpaRepository.findAvailableForAssignment(DriverStatus.AVAILABLE, PageRequest.of(0, 1));
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(driverPersistenceMapper.toResult(candidates.getFirst()));
    }

    @Override
    public Optional<DriverResult> findById(UUID driverId) {
        return driverJpaRepository.findById(driverId).map(driverPersistenceMapper::toResult);
    }

    @Override
    public void save(DriverResult driver) {
        DriverEntity entity = driverJpaRepository.findById(driver.id()).orElseThrow();
        driverPersistenceMapper.copyToEntity(driver, entity);
        driverJpaRepository.save(entity);
    }
}
