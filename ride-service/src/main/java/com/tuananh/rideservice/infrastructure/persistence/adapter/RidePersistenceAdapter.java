package com.tuananh.rideservice.infrastructure.persistence.adapter;

import com.tuananh.rideservice.application.port.in.command.CreateRideCommand;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import com.tuananh.rideservice.application.port.out.RidePersistencePort;
import com.tuananh.rideservice.infrastructure.persistence.entity.RideEntity;
import com.tuananh.rideservice.infrastructure.persistence.mapper.RidePersistenceMapper;
import com.tuananh.rideservice.infrastructure.persistence.repository.RideJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RidePersistenceAdapter implements RidePersistencePort {

    RideJpaRepository rideJpaRepository;
    RidePersistenceMapper ridePersistenceMapper;

    @Override
    public Optional<RideResult> findById(UUID rideId) {
        return rideJpaRepository.findById(rideId).map(ridePersistenceMapper::toResult);
    }

    @Override
    public Optional<RideResult> findByIdempotencyKey(String idempotencyKey) {
        return rideJpaRepository.findByIdempotencyKey(idempotencyKey).map(ridePersistenceMapper::toResult);
    }

    @Override
    public RideResult saveNew(CreateRideCommand command) {
        RideEntity entity = ridePersistenceMapper.toNewEntity(command);
        RideEntity saved = rideJpaRepository.save(entity);
        return ridePersistenceMapper.toResult(saved);
    }

    @Override
    public void update(RideResult ride) {
        RideEntity entity =
                rideJpaRepository.findById(ride.id()).orElseThrow();
        ridePersistenceMapper.copyToEntity(ride, entity);
        rideJpaRepository.save(entity);
    }
}
