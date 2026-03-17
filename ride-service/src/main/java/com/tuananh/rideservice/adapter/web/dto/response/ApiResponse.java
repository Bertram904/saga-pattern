package com.tuananh.rideservice.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuananh.rideservice.domain.error.AppError;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * Uniform API envelope for success responses (errors use {@link com.tuananh.rideservice.adapter.web.dto.error.ApiErrorBody}).
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    boolean success;
    String code;
    String message;
    T data;
    Instant timestamp;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(AppError.SUCCESS.getCode())
                .message("OK")
                .data(data)
                .timestamp(Instant.now())
                .build();
    }
}
