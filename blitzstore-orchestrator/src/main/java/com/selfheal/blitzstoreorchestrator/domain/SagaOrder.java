package com.selfheal.blitzstoreorchestrator.domain;

import events.SagaStep;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class SagaOrder {

    @Id
    private Long sagaId; // == orderId

    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    @Enumerated(EnumType.STRING)
    private SagaStep step;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus payment;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus stock;

    @Version
    private Long version;
}
