package com.tuananh.rideservice.domain.exception;

import com.tuananh.rideservice.domain.error.AppError;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

/**
 * Base for all business-rule failures (maps to HTTP via {@link AppError#getHttpStatus()}).
 */
@Getter
public class DomainException extends RuntimeException {

    private final AppError appError;
    private final Map<String, Object> details;

    public DomainException(AppError appError) {
        super(appError.getCode());
        this.appError = appError;
        this.details = Collections.emptyMap();
    }

    public DomainException(AppError appError, String message) {
        super(message);
        this.appError = appError;
        this.details = Collections.emptyMap();
    }

    public DomainException(AppError appError, String message, Map<String, Object> details) {
        super(message);
        this.appError = appError;
        this.details = details != null ? Map.copyOf(details) : Collections.emptyMap();
    }

    public DomainException(AppError appError, String message, Throwable cause) {
        super(message, cause);
        this.appError = appError;
        this.details = Collections.emptyMap();
    }
}
