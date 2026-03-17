package com.tuananh.rideservice.infrastructure.messaging;

import com.tuananh.rideservice.infrastructure.persistence.entity.OutboxMessageEntity;
import com.tuananh.rideservice.infrastructure.persistence.repository.OutboxMessageJpaRepository;
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

/** Polling relay: unpublished outbox rows → Kafka (at-least-once; downstream idempotent by `eventId`). */
@ConditionalOnProperty(prefix = "app.outbox", name = "polling-relay-enabled", havingValue = "true", matchIfMissing = true)
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxRelayService {

    OutboxMessageJpaRepository outboxMessageJpaRepository;
    KafkaTemplate<String, String> kafkaTemplate;
    OutboxMarker outboxMarker;

    @Scheduled(fixedDelayString = "${app.outbox.relay-delay-ms:500}")
    public void relay() {
        for (OutboxMessageEntity message :
                outboxMessageJpaRepository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                String topic = topicFor(message.getEventType());
                kafkaTemplate
                        .send(topic, message.getAggregateId(), message.getPayload())
                        .get(30, TimeUnit.SECONDS);
                outboxMarker.markPublished(message.getId());
                log.debug("Relayed outbox {} to topic {}", message.getId(), topic);
            } catch (Exception e) {
                log.warn("Outbox relay failed for id={} (will retry): {}", message.getId(), e.getMessage());
            }
        }
    }

    private static String topicFor(String eventType) {
        return switch (eventType) {
            case EventTypes.RIDE_REQUESTED -> KafkaTopics.RIDE_REQUESTED;
            case EventTypes.RELEASE_DRIVER -> KafkaTopics.DRIVER_RELEASE;
            default -> throw new IllegalStateException("Unknown outbox event type: " + eventType);
        };
    }
}
