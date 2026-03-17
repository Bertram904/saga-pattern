package com.tuananh.rideservice.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.rideservice.application.port.in.RideSagaPort;
import com.tuananh.rideservice.infrastructure.persistence.entity.ProcessedInboundEventEntity;
import com.tuananh.rideservice.infrastructure.persistence.repository.ProcessedInboundEventJpaRepository;
import com.tuananh.shared.events.*;
import com.tuananh.shared.messaging.KafkaTopics;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RideSagaKafkaListener {

    ObjectMapper objectMapper;
    ProcessedInboundEventJpaRepository processedInboundEventJpaRepository;
    RideSagaPort rideSagaPort;

    @KafkaListener(topics = KafkaTopics.DRIVER_ASSIGNED, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onDriverAssigned(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), DriverAssignedPayload.class);
        rideSagaPort.onDriverAssigned(payload);
        processedInboundEventJpaRepository.save(
                new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }

    @KafkaListener(topics = KafkaTopics.DRIVER_NOT_FOUND, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onDriverNotFound(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), DriverNotFoundPayload.class);
        rideSagaPort.onDriverNotFound(payload);
        processedInboundEventJpaRepository.save(
                new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_AUTHORIZED, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onPaymentAuthorized(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), PaymentAuthorizedPayload.class);
        rideSagaPort.onPaymentAuthorized(payload);
        processedInboundEventJpaRepository.save(
                new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onPaymentFailed(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), PaymentFailedPayload.class);
        rideSagaPort.onPaymentFailed(payload);
        processedInboundEventJpaRepository.save(
                new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }
}
