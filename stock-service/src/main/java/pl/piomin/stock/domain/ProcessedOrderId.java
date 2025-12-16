package pl.piomin.stock.domain;

import events.EventType;
import java.io.Serializable;
import java.util.Objects;

public class ProcessedOrderId implements Serializable {

    private Long sagaId;
    private EventType eventType;

    public ProcessedOrderId() {}

    public ProcessedOrderId(Long sagaId, EventType eventType) {
        this.sagaId = sagaId;
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessedOrderId)) return false;
        ProcessedOrderId that = (ProcessedOrderId) o;
        return Objects.equals(sagaId, that.sagaId) &&
                eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sagaId, eventType);
    }
}
