package com.tuananh.paymentservice.infrastructure.messaging;

import com.tuananh.paymentservice.infrastructure.persistence.entity.PaymentOutboxMessageEntity;
import com.tuananh.paymentservice.infrastructure.persistence.repository.PaymentOutboxMessageJpaRepository;
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
public class PaymentOutboxMarker {

    PaymentOutboxMessageJpaRepository paymentOutboxMessageJpaRepository;

    @Transactional
    public void markPublished(UUID outboxId) {
        PaymentOutboxMessageEntity message = paymentOutboxMessageJpaRepository.findById(outboxId).orElse(null);
        if (message == null || message.getPublishedAt() != null) {
            return;
        }
        message.setPublishedAt(LocalDateTime.now());
        paymentOutboxMessageJpaRepository.save(message);
    }
}
