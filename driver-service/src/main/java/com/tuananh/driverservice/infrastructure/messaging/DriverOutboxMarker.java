package com.tuananh.driverservice.infrastructure.messaging;

import com.tuananh.driverservice.infrastructure.persistence.entity.DriverOutboxMessageEntity;
import com.tuananh.driverservice.infrastructure.persistence.repository.DriverOutboxMessageJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/** Marks outbox rows published after successful Kafka send (at-least-once safe). */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverOutboxMarker {

    DriverOutboxMessageJpaRepository driverOutboxMessageJpaRepository;

    @Transactional
    public void markPublished(UUID outboxId) {
        DriverOutboxMessageEntity message = driverOutboxMessageJpaRepository.findById(outboxId).orElse(null);
        if (message == null || message.getPublishedAt() != null) {
            return;
        }
        message.setPublishedAt(LocalDateTime.now());
        driverOutboxMessageJpaRepository.save(message);
    }
}
