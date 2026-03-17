package com.tuananh.driverservice.domain.exception;

/** Thrown when outbox payload cannot be serialized (should abort the transaction). */
public class EventSerializationException extends RuntimeException {

    public EventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
