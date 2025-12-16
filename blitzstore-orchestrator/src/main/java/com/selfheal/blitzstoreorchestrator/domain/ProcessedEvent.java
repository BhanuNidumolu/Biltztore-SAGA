package com.selfheal.blitzstoreorchestrator.domain;

import events.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "processed_event",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sagaId", "eventType"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sagaId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Instant processedAt = Instant.now();
}
