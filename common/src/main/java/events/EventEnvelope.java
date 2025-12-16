package events;

import java.time.Instant;
import java.util.UUID;

public class EventEnvelope<T> {

    private String eventId = UUID.randomUUID().toString();
    private EventType eventType;
    private String sagaId;      // orderId
    private String source;
    private Instant timestamp = Instant.now();
    private T payload;

    public EventEnvelope() {}

    public EventEnvelope(EventType eventType, String sagaId, String source, T payload) {
        this.eventType = eventType;
        this.sagaId = sagaId;
        this.source = source;
        this.payload = payload;
    }

    public String getEventId() { return eventId; }
    public EventType getEventType() { return eventType; }
    public String getSagaId() { return sagaId; }
    public String getSource() { return source; }
    public Instant getTimestamp() { return timestamp; }
    public T getPayload() { return payload; }
}
