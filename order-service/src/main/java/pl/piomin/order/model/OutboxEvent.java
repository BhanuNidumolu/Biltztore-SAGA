package pl.piomin.order.model;

import events.EventType;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity
@Table(
        name = "outbox_event",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aggregateId", "eventType"})
)
@Getter
@Setter
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private Long aggregateId; // orderId

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Lob
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.NEW;

    private int retryCount;
    private String lastError;

    @Version
    private Long version;

    private Instant createdAt = Instant.now();
}
