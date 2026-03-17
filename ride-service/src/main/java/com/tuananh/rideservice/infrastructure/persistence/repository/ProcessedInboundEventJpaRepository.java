package com.tuananh.rideservice.infrastructure.persistence.repository;

import com.tuananh.rideservice.infrastructure.persistence.entity.ProcessedInboundEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedInboundEventJpaRepository extends JpaRepository<ProcessedInboundEventEntity, String> {}
