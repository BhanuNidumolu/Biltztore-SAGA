package pl.piomin.payment.repository;

import events.EventType;
import org.springframework.data.repository.CrudRepository;
import pl.piomin.payment.domain.ProcessedOrder;
import pl.piomin.payment.domain.ProcessedOrderId;

public interface ProcessedOrderRepository
        extends CrudRepository<ProcessedOrder, ProcessedOrderId> {

    boolean existsBySagaIdAndEventType(Long sagaId, EventType eventType);
}
