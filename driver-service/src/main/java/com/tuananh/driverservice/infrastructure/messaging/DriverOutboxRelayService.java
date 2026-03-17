package com.tuananh.driverservice.infrastructure.messaging;

import com.tuananh.driverservice.infrastructure.persistence.entity.DriverOutboxMessageEntity;
import com.tuananh.driverservice.infrastructure.persistence.repository.DriverOutboxMessageJpaRepository;
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

/** Polling publisher for driver outbox (disable when using CDC-only pipeline; see docs). */
@ConditionalOnProperty(prefix = "app.outbox", name = "polling-relay-enabled", havingValue = "true", matchIfMissing = true)
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverOutboxRelayService {

    DriverOutboxMessageJpaRepository driverOutboxMessageJpaRepository;
    KafkaTemplate<String, String> kafkaTemplate;
    DriverOutboxMarker driverOutboxMarker;

    @Scheduled(fixedDelayString = "${app.outbox.relay-delay-ms:500}")
    public void relay() {
        for (DriverOutboxMessageEntity message :
                driverOutboxMessageJpaRepository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                String topic = topicFor(message.getEventType());
                kafkaTemplate
                        .send(topic, message.getAggregateId(), message.getPayload())
                        .get(30, TimeUnit.SECONDS);
                driverOutboxMarker.markPublished(message.getId());
            } catch (Exception e) {
                log.warn("Driver outbox relay failed id={}: {}", message.getId(), e.getMessage());
            }
        }
    }

    private static String topicFor(String eventType) {
        return switch (eventType) {
            case EventTypes.DRIVER_ASSIGNED -> KafkaTopics.DRIVER_ASSIGNED;
            case EventTypes.DRIVER_NOT_FOUND -> KafkaTopics.DRIVER_NOT_FOUND;
            default -> throw new IllegalStateException("Unknown driver outbox event: " + eventType);
        };
    }
}
