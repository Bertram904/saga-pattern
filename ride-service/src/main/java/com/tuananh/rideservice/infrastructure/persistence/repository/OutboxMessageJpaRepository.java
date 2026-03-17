package com.tuananh.rideservice.infrastructure.persistence.repository;

import com.tuananh.rideservice.infrastructure.persistence.entity.OutboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxMessageJpaRepository extends JpaRepository<OutboxMessageEntity, UUID> {
    List<OutboxMessageEntity> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
