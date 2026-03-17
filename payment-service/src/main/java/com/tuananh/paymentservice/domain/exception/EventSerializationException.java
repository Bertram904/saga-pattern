package com.tuananh.paymentservice.domain.exception;

/** Outbox JSON serialization failure — rolls back the transaction. */
public class EventSerializationException extends RuntimeException {

    public EventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
