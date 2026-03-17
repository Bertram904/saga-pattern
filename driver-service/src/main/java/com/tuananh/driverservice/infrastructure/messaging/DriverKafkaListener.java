package com.tuananh.driverservice.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.driverservice.application.port.in.DriverSagaPort;
import com.tuananh.driverservice.infrastructure.persistence.entity.ProcessedInboundEventEntity;
import com.tuananh.driverservice.infrastructure.persistence.repository.ProcessedInboundEventJpaRepository;
import com.tuananh.shared.events.EventEnvelope;
import com.tuananh.shared.events.ReleaseDriverPayload;
import com.tuananh.shared.events.RideRequestedPayload;
import com.tuananh.shared.messaging.KafkaTopics;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/** Inbound adapter: Kafka → idempotent saga port (same eventId is skipped). */
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverKafkaListener {

    ObjectMapper objectMapper;
    ProcessedInboundEventJpaRepository processedInboundEventJpaRepository;
    DriverSagaPort driverSagaPort;

    @KafkaListener(topics = KafkaTopics.RIDE_REQUESTED, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onRideRequested(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), RideRequestedPayload.class);
        driverSagaPort.onRideRequested(payload);
        processedInboundEventJpaRepository.save(new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }

    @KafkaListener(topics = KafkaTopics.DRIVER_RELEASE, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onReleaseDriver(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), ReleaseDriverPayload.class);
        driverSagaPort.onReleaseDriver(payload);
        processedInboundEventJpaRepository.save(new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }
}
