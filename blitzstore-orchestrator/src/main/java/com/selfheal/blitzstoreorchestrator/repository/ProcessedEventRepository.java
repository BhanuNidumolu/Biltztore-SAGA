package com.selfheal.blitzstoreorchestrator.repository;

import com.selfheal.blitzstoreorchestrator.domain.ProcessedEvent;
import events.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, Long> {

    boolean existsBySagaIdAndEventType(
            Long sagaId,
            EventType eventType
    );
}
