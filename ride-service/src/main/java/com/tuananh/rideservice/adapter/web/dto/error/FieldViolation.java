package com.tuananh.rideservice.adapter.web.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldViolation {
    String field;
    String message;
    Object rejectedValue;
}
