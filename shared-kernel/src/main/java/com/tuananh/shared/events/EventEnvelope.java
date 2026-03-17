package com.tuananh.shared.events;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Canonical wire format for Kafka values: at-least-once delivery + idempotent consumers via {@code eventId}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventEnvelope<T>(
        String eventId,
        String eventType,
        String correlationId,
        Instant occurredAt,
        T payload
) {}
