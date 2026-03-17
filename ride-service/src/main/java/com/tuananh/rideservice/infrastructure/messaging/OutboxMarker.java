package com.tuananh.rideservice.infrastructure.messaging;

import com.tuananh.rideservice.infrastructure.persistence.entity.OutboxMessageEntity;
import com.tuananh.rideservice.infrastructure.persistence.repository.OutboxMessageJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxMarker {

    OutboxMessageJpaRepository outboxMessageJpaRepository;

    @Transactional
    public void markPublished(UUID outboxId) {
        OutboxMessageEntity message = outboxMessageJpaRepository.findById(outboxId).orElse(null);
        if (message == null || message.getPublishedAt() != null) {
            return;
        }
        message.setPublishedAt(LocalDateTime.now());
        outboxMessageJpaRepository.save(message);
    }
}
