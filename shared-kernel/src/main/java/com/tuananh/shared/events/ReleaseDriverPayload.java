package com.tuananh.shared.events;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReleaseDriverPayload(UUID rideId, UUID driverId, String reason) {}
