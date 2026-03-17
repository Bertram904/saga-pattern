package com.tuananh.rideservice.infrastructure.persistence.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuananh.rideservice.application.port.in.result.RideResult;
import com.tuananh.rideservice.application.port.out.OutboxMessagePort;
import com.tuananh.rideservice.domain.exception.EventSerializationException;
import com.tuananh.rideservice.infrastructure.persistence.entity.OutboxMessageEntity;
import com.tuananh.rideservice.infrastructure.persistence.repository.OutboxMessageJpaRepository;
import com.tuananh.shared.events.EventEnvelope;
import com.tuananh.shared.events.ReleaseDriverPayload;
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
public class OutboxMessageAdapter implements OutboxMessagePort {

    OutboxMessageJpaRepository outboxMessageJpaRepository;
    ObjectMapper objectMapper;

    @Override
    public void enqueueRideRequested(RideResult ride) {
        try {
            var payload = new RideRequestedPayload(
                    ride.id(),
                    ride.passengerId(),
                    ride.pickupLocation(),
                    ride.dropOffLocation(),
                    ride.estimatedAmount());
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.RIDE_REQUESTED,
                    ride.id().toString(),
                    Instant.now(),
                    payload);
            String json = objectMapper.writeValueAsString(envelope);
            outboxMessageJpaRepository.save(OutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("ride")
                    .aggregateId(ride.id().toString())
                    .eventType(EventTypes.RIDE_REQUESTED)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize RIDE_REQUESTED envelope", e);
        }
    }

    @Override
    public void enqueueReleaseDriver(UUID rideId, UUID driverId, String reason) {
        try {
            var payload = new ReleaseDriverPayload(rideId, driverId, reason);
            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    EventTypes.RELEASE_DRIVER,
                    rideId.toString(),
                    Instant.now(),
                    payload);
            String json = objectMapper.writeValueAsString(envelope);
            outboxMessageJpaRepository.save(OutboxMessageEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("ride")
                    .aggregateId(rideId.toString())
                    .eventType(EventTypes.RELEASE_DRIVER)
                    .payload(json)
                    .build());
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("Failed to serialize RELEASE_DRIVER envelope", e);
        }
    }
}
