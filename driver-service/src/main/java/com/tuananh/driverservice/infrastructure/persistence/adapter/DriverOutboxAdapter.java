package com.tuananh.driverservice.infrastructure.persistence.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.driverservice.application.port.out.DriverOutboxPort;
import com.tuananh.driverservice.domain.exception.EventSerializationException;
import com.tuananh.driverservice.infrastructure.persistence.entity.DriverOutboxMessageEntity;
import com.tuananh.driverservice.infrastructure.persistence.repository.DriverOutboxMessageJpaRepository;
import com.tuananh.shared.events.DriverAssignedPayload;
import com.tuananh.shared.events.DriverNotFoundPayload;
import com.tuananh.shared.events.EventEnvelope;
import com.tuananh.shared.events.RideRequestedPayload;
import com.tuananh.shared.messaging.EventTypes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DriverOutboxAdapter implements DriverOutboxPort {

    DriverOutboxMessageJpaRepository outboxMessageJpaRepository;
    ObjectMapper objectMapper;

    @Override
    public void enqueueDriverAssigned(RideRequestedPayload rideContext, UUID driverId) {
        try {
            var body = new DriverAssignedPayload(
                    rideContext.rideId(), driverId, rideContext.passengerId(), rideContext.estimatedAmount());
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.DRIVER_ASSIGNED,
                    rideContext.rideId().toString(),
                    Instant.now(),
                    body);
            String json = objectMapper.writeValueAsString(envelope);
            outboxMessageJpaRepository.save(DriverOutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("driver")
                    .aggregateId(rideContext.rideId().toString())
                    .eventType(EventTypes.DRIVER_ASSIGNED)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize DRIVER_ASSIGNED", e);
        }
    }

    @Override
    public void enqueueDriverNotFound(UUID rideId) {
        try {
            var body = new DriverNotFoundPayload(rideId, "NO_AVAILABLE_DRIVER");
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.DRIVER_NOT_FOUND,
                    rideId.toString(),
                    Instant.now(),
                    body);
            String json = objectMapper.writeValueAsString(envelope);
            outboxMessageJpaRepository.save(DriverOutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("driver")
                    .aggregateId(rideId.toString())
                    .eventType(EventTypes.DRIVER_NOT_FOUND)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize DRIVER_NOT_FOUND", e);
        }
    }
}
