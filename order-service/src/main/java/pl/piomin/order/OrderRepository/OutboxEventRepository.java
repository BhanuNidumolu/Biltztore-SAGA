package pl.piomin.order.OrderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piomin.order.model.OutboxEvent;
import pl.piomin.order.model.OutboxStatus;

import java.util.List;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
