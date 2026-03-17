package com.tuananh.driverservice.bootstrap;

import com.tuananh.driverservice.domain.model.DriverStatus;
import com.tuananh.driverservice.infrastructure.persistence.entity.DriverEntity;
import com.tuananh.driverservice.infrastructure.persistence.repository.DriverJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** Demo seed data (replace with real driver pool / geo index in production). */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverDataInitializer implements ApplicationRunner {

    DriverJpaRepository driverJpaRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (driverJpaRepository.count() > 0) {
            return;
        }
        driverJpaRepository.save(DriverEntity.builder().name("Alice").status(DriverStatus.AVAILABLE).build());
        driverJpaRepository.save(DriverEntity.builder().name("Bob").status(DriverStatus.AVAILABLE).build());
        driverJpaRepository.save(DriverEntity.builder().name("Carol").status(DriverStatus.AVAILABLE).build());
    }
}
