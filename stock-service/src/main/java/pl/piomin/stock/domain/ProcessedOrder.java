package pl.piomin.stock.domain;

import events.EventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ProcessedOrderId.class)
@Getter
@NoArgsConstructor
public class ProcessedOrder {

    @Id
    private Long sagaId;

    @Id
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public ProcessedOrder(Long sagaId, EventType eventType) {
        this.sagaId = sagaId;
        this.eventType = eventType;
    }
}
