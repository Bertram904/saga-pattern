package com.tuananh.paymentservice.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.paymentservice.application.port.in.PaymentSagaPort;
import com.tuananh.paymentservice.infrastructure.persistence.entity.ProcessedInboundEventEntity;
import com.tuananh.paymentservice.infrastructure.persistence.repository.ProcessedInboundEventJpaRepository;
import com.tuananh.shared.events.DriverAssignedPayload;
import com.tuananh.shared.events.EventEnvelope;
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
public class PaymentKafkaListener {

    ObjectMapper objectMapper;
    ProcessedInboundEventJpaRepository processedInboundEventJpaRepository;
    PaymentSagaPort paymentSagaPort;

    @KafkaListener(topics = KafkaTopics.DRIVER_ASSIGNED, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void onDriverAssigned(String value) throws Exception {
        var envelope = objectMapper.readValue(value, new TypeReference<EventEnvelope<JsonNode>>() {});
        if (processedInboundEventJpaRepository.existsById(envelope.eventId())) {
            return;
        }
        var payload = objectMapper.treeToValue(envelope.payload(), DriverAssignedPayload.class);
        paymentSagaPort.onDriverAssigned(payload);
        processedInboundEventJpaRepository.save(new ProcessedInboundEventEntity(envelope.eventId(), Instant.now()));
    }
}
