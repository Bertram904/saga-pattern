package com.tuananh.rideservice.infrastructure.persistence.repository;

import com.tuananh.rideservice.infrastructure.persistence.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RideJpaRepository extends JpaRepository<RideEntity, UUID> {
    Optional<RideEntity> findByIdempotencyKey(String idempotencyKey);
}
