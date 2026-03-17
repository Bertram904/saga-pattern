package com.tuananh.rideservice.adapter.web.rest;

import com.tuananh.rideservice.adapter.web.dto.request.CreateRideRequest;
import com.tuananh.rideservice.adapter.web.dto.response.ApiResponse;
import com.tuananh.rideservice.adapter.web.dto.response.RideResponse;
import com.tuananh.rideservice.adapter.web.mapper.RideWebMapper;
import com.tuananh.rideservice.application.port.in.CreateRideUseCase;
import com.tuananh.rideservice.application.port.in.GetRideUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

/**
 * Thin adapter: maps HTTP ↔ application commands/results only.
 */
@RestController
@RequestMapping("/api/v1/rides")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RideController {

    CreateRideUseCase createRideUseCase;
    GetRideUseCase getRideUseCase;
    RideWebMapper rideWebMapper;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<RideResponse>> bookRide(
            @RequestHeader("Idempotency-Key") @NotBlank(message = "Idempotency-Key header is required") String idempotencyKey,
            @Valid @RequestBody CreateRideRequest request) {
        var result = createRideUseCase.createRide(rideWebMapper.toCommand(request, idempotencyKey));
        RideResponse body = rideWebMapper.toResponse(result);
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/rides/")
                .path(body.getId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(ApiResponse.ok(body));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<ApiResponse<RideResponse>> getRide(@PathVariable UUID rideId) {
        var result = getRideUseCase.getRide(rideId);
        return ResponseEntity.ok(ApiResponse.ok(rideWebMapper.toResponse(result)));
    }
}
