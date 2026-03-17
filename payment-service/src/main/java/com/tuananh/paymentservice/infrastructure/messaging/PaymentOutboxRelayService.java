package com.tuananh.paymentservice.infrastructure.messaging;

import com.tuananh.paymentservice.infrastructure.persistence.entity.PaymentOutboxMessageEntity;
import com.tuananh.paymentservice.infrastructure.persistence.repository.PaymentOutboxMessageJpaRepository;
import com.tuananh.shared.messaging.EventTypes;
import com.tuananh.shared.messaging.KafkaTopics;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/** Polling publisher for payment outbox (toggle via app.outbox.polling-relay-enabled). */
@ConditionalOnProperty(prefix = "app.outbox", name = "polling-relay-enabled", havingValue = "true", matchIfMissing = true)
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentOutboxRelayService {

    PaymentOutboxMessageJpaRepository paymentOutboxMessageJpaRepository;
    KafkaTemplate<String, String> kafkaTemplate;
    PaymentOutboxMarker paymentOutboxMarker;

    @Scheduled(fixedDelayString = "${app.outbox.relay-delay-ms:500}")
    public void relay() {
        for (PaymentOutboxMessageEntity message :
                paymentOutboxMessageJpaRepository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                String topic = topicFor(message.getEventType());
                kafkaTemplate
                        .send(topic, message.getAggregateId(), message.getPayload())
                        .get(30, TimeUnit.SECONDS);
                paymentOutboxMarker.markPublished(message.getId());
            } catch (Exception e) {
                log.warn("Payment outbox relay failed id={}: {}", message.getId(), e.getMessage());
            }
        }
    }

    private static String topicFor(String eventType) {
        return switch (eventType) {
            case EventTypes.PAYMENT_AUTHORIZED -> KafkaTopics.PAYMENT_AUTHORIZED;
            case EventTypes.PAYMENT_FAILED -> KafkaTopics.PAYMENT_FAILED;
            default -> throw new IllegalStateException("Unknown payment outbox event: " + eventType);
        };
    }
}
