package pl.piomin.payment.domain;

import events.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ProcessedOrderId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedOrder {

    @Id
    private Long sagaId;

    @Id
    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
