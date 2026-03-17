package com.tuananh.rideservice.domain.exception;

import com.tuananh.rideservice.domain.error.AppError;

public class EventSerializationException extends DomainException {

    public EventSerializationException(String message, Throwable cause) {
        super(AppError.EVENT_SERIALIZATION_FAILED, message, cause);
    }
}
