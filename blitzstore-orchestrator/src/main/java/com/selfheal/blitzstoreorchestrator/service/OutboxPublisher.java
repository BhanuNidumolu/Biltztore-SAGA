package com.selfheal.blitzstoreorchestrator.service;

import com.selfheal.blitzstoreorchestrator.domain.OutboxEvent;
import com.selfheal.blitzstoreorchestrator.domain.OutboxStatus;
import com.selfheal.blitzstoreorchestrator.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository repo;
    private final KafkaTemplate<String, String> kafka;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publish() {

        List<OutboxEvent> events =
                repo.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);

        for (OutboxEvent e : events) {
            kafka.send(
                    e.getTopic(),
                    String.valueOf(e.getAggregateId()),
                    e.getPayload()
            );
            e.setStatus(OutboxStatus.SENT);
            repo.save(e);
        }
    }
}
