package com.tuananh.paymentservice.infrastructure.persistence.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.paymentservice.application.port.out.PaymentOutboxPort;
import com.tuananh.paymentservice.domain.exception.EventSerializationException;
import com.tuananh.paymentservice.infrastructure.persistence.entity.PaymentOutboxMessageEntity;
import com.tuananh.paymentservice.infrastructure.persistence.repository.PaymentOutboxMessageJpaRepository;
import com.tuananh.shared.events.DriverAssignedPayload;
import com.tuananh.shared.events.EventEnvelope;
import com.tuananh.shared.events.PaymentAuthorizedPayload;
import com.tuananh.shared.events.PaymentFailedPayload;
import com.tuananh.shared.messaging.EventTypes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentOutboxAdapter implements PaymentOutboxPort {

    PaymentOutboxMessageJpaRepository paymentOutboxMessageJpaRepository;
    ObjectMapper objectMapper;

    @Override
    public void enqueuePaymentAuthorized(DriverAssignedPayload context, BigDecimal reservedAmount) {
        try {
            var body = new PaymentAuthorizedPayload(
                    context.rideId(), context.driverId(), context.passengerId(), reservedAmount);
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.PAYMENT_AUTHORIZED,
                    context.rideId().toString(),
                    Instant.now(),
                    body);
            String json = objectMapper.writeValueAsString(envelope);
            paymentOutboxMessageJpaRepository.save(PaymentOutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("payment")
                    .aggregateId(context.rideId().toString())
                    .eventType(EventTypes.PAYMENT_AUTHORIZED)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize PAYMENT_AUTHORIZED", e);
        }
    }

    @Override
    public void enqueuePaymentFailed(DriverAssignedPayload context, String reason) {
        try {
            var body = new PaymentFailedPayload(
                    context.rideId(),
                    context.driverId(),
                    context.passengerId(),
                    context.estimatedAmount(),
                    reason);
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.PAYMENT_FAILED,
                    context.rideId().toString(),
                    Instant.now(),
                    body);
            String json = objectMapper.writeValueAsString(envelope);
            paymentOutboxMessageJpaRepository.save(PaymentOutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("payment")
                    .aggregateId(context.rideId().toString())
                    .eventType(EventTypes.PAYMENT_FAILED)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize PAYMENT_FAILED", e);
        }
    }
}
