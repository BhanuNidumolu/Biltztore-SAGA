package pl.piomin.stock.repository;

import events.EventType;
import org.springframework.data.repository.CrudRepository;
import pl.piomin.stock.domain.ProcessedOrder;
import pl.piomin.stock.domain.ProcessedOrderId;

public interface ProcessedOrderRepository
        extends CrudRepository<ProcessedOrder, ProcessedOrderId> {

    boolean existsBySagaIdAndEventType(Long sagaId, EventType eventType);
}
