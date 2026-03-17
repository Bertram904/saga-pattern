package com.tuananh.paymentservice.infrastructure.persistence.repository;

import com.tuananh.paymentservice.infrastructure.persistence.entity.PaymentOutboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentOutboxMessageJpaRepository extends JpaRepository<PaymentOutboxMessageEntity, UUID> {
    List<PaymentOutboxMessageEntity> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
