package com.tuananh.driverservice.infrastructure.persistence.repository;

import com.tuananh.driverservice.infrastructure.persistence.entity.DriverOutboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverOutboxMessageJpaRepository extends JpaRepository<DriverOutboxMessageEntity, UUID> {
    List<DriverOutboxMessageEntity> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
