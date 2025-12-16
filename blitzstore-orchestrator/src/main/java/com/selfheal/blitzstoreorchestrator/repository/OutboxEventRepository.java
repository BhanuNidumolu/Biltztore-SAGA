package com.selfheal.blitzstoreorchestrator.repository;

import com.selfheal.blitzstoreorchestrator.domain.OutboxEvent;
import com.selfheal.blitzstoreorchestrator.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, Long> {

    /**
     * FIFO publishing
     */
    List<OutboxEvent>
    findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    /**
     * Safety check (optional, but strong)
     */
    boolean existsByAggregateIdAndEventType(
            Long aggregateId,
            Enum<?> eventType
    );
}
