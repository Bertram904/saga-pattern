package com.tuananh.rideservice.adapter.web.advice;

import com.tuananh.rideservice.adapter.web.dto.error.ApiErrorBody;
import com.tuananh.rideservice.adapter.web.dto.error.FieldViolation;
import com.tuananh.rideservice.domain.error.AppError;
import com.tuananh.rideservice.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Maps domain/application failures to HTTP + stable error contract (enterprise API).
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorBody> handleDomain(DomainException ex, HttpServletRequest request) {
        AppError err = ex.getAppError();
        String traceId = traceId(request);
        log.warn("[{}] Domain error {}: {}", traceId, err.getCode(), ex.getMessage());
        ApiErrorBody body = ApiErrorBody.builder()
                .type("about:blank")
                .title(err.getHttpStatus().getReasonPhrase())
                .code(err.getCode())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .traceId(traceId)
                .timestamp(java.time.Instant.now())
                .build();
        return ResponseEntity.status(err.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON.toString())
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorBody> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = traceId(request);
        List<FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toViolation)
                .collect(Collectors.toList());
        String detail = violations.isEmpty() ? "Validation failed" : violations.getFirst().getMessage();
        log.warn("[{}] Validation failed: {}", traceId, violations);
        ApiErrorBody body = ApiErrorBody.builder()
                .type("about:blank")
                .title(AppError.VALIDATION_FAILED.getHttpStatus().getReasonPhrase())
                .code(AppError.VALIDATION_FAILED.getCode())
                .detail(detail)
                .instance(request.getRequestURI())
                .traceId(traceId)
                .violations(violations)
                .timestamp(java.time.Instant.now())
                .build();
        return ResponseEntity.status(AppError.VALIDATION_FAILED.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON.toString())
                .body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorBody> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        String traceId = traceId(request);
        List<FieldViolation> violations = ex.getConstraintViolations().stream()
                .map(v -> FieldViolation.builder()
                        .field(v.getPropertyPath() != null ? v.getPropertyPath().toString() : null)
                        .message(v.getMessage())
                        .rejectedValue(v.getInvalidValue())
                        .build())
                .collect(Collectors.toList());
        ApiErrorBody body = ApiErrorBody.builder()
                .type("about:blank")
                .title(AppError.VALIDATION_FAILED.getHttpStatus().getReasonPhrase())
                .code(AppError.VALIDATION_FAILED.getCode())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .traceId(traceId)
                .violations(violations)
                .timestamp(java.time.Instant.now())
                .build();
        return ResponseEntity.status(AppError.VALIDATION_FAILED.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON.toString())
                .body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorBody> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String traceId = traceId(request);
        ApiErrorBody body = ApiErrorBody.builder()
                .type("about:blank")
                .title(AppError.VALIDATION_FAILED.getHttpStatus().getReasonPhrase())
                .code(AppError.VALIDATION_FAILED.getCode())
                .detail("Malformed JSON or unsupported content type")
                .instance(request.getRequestURI())
                .traceId(traceId)
                .timestamp(java.time.Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON.toString())
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorBody> handleAny(Exception ex, HttpServletRequest request) {
        String traceId = traceId(request);
        log.error("[{}] Unhandled error", traceId, ex);
        ApiErrorBody body = ApiErrorBody.from(
                AppError.INTERNAL_ERROR,
                AppError.INTERNAL_ERROR.getHttpStatus().getReasonPhrase(),
                request.getRequestURI(),
                traceId);
        return ResponseEntity.status(AppError.INTERNAL_ERROR.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON.toString())
                .body(body);
    }

    private FieldViolation toViolation(FieldError fe) {
        return FieldViolation.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .rejectedValue(fe.getRejectedValue())
                .build();
    }

    private static String traceId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Request-Id")).filter(s -> !s.isBlank())
                .orElseGet(() -> UUID.randomUUID().toString());
    }
}
