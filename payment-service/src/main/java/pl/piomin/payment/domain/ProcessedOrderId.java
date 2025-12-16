package pl.piomin.payment.domain;

import events.EventType;

import java.io.Serializable;
import java.util.Objects;

public class ProcessedOrderId implements Serializable {

    private Long sagaId;
    private EventType eventType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessedOrderId that)) return false;
        return Objects.equals(sagaId, that.sagaId)
                && eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sagaId, eventType);
    }
}
