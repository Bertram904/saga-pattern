package com.tuananh.rideservice.adapter.web.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuananh.rideservice.domain.error.AppError;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

/**
 * Error contract aligned with Problem Details (RFC 7807) extensions: machine code + field violations.
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorBody {
    String type;
    String title;
    String code;
    String detail;
    String instance;
    String traceId;
    Instant timestamp;
    List<FieldViolation> violations;

    public static ApiErrorBody from(AppError error, String detail, String instance, String traceId) {
        return ApiErrorBody.builder()
                .type("about:blank")
                .title(error.getHttpStatus().getReasonPhrase())
                .code(error.getCode())
                .detail(detail != null ? detail : error.getHttpStatus().getReasonPhrase())
                .instance(instance)
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();
    }
}
