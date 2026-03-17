package com.tuananh.driverservice.infrastructure.persistence.repository;

import com.tuananh.driverservice.infrastructure.persistence.entity.ProcessedInboundEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedInboundEventJpaRepository extends JpaRepository<ProcessedInboundEventEntity, String> {}
