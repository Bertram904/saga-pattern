package com.tuananh.paymentservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "processed_inbound_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedInboundEventEntity {

    @Id
    @Column(length = 64, nullable = false)
    private String eventId;

    private Instant processedAt;
}
