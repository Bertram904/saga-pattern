package com.tuananh.rideservice.domain.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * Stable business error codes for APIs and logs (i18n-ready via {@link #getMessageKey()}).
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AppError {

    SUCCESS("COMMON.SUCCESS", "SUCCESS", HttpStatus.OK),
    VALIDATION_FAILED("COMMON.VALIDATION_FAILED", "VALIDATION_FAILED", HttpStatus.BAD_REQUEST),
    RIDE_NOT_FOUND("RIDE.NOT_FOUND", "RIDE_NOT_FOUND", HttpStatus.NOT_FOUND),
    EVENT_SERIALIZATION_FAILED("RIDE.EVENT_SERIALIZATION_FAILED", "EVENT_SERIALIZATION_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_ERROR("COMMON.INTERNAL_ERROR", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

    /** Message bundle key (future i18n). */
    String messageKey;
    /** Stable machine-readable code returned to clients. */
    String code;
    HttpStatus httpStatus;
}
