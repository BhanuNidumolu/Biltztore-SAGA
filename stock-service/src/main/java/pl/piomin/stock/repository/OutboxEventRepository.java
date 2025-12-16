package pl.piomin.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piomin.stock.domain.OutboxEvent;
import pl.piomin.stock.domain.OutboxStatus;

import java.util.List;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
