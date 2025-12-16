package com.selfheal.blitzstoreorchestrator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfheal.blitzstoreorchestrator.domain.*;
import com.selfheal.blitzstoreorchestrator.repository.OutboxEventRepository;
import com.selfheal.blitzstoreorchestrator.repository.ProcessedEventRepository;
import com.selfheal.blitzstoreorchestrator.repository.SagaOrderRepository;
import dto.OrderDTO;
import events.EventEnvelope;
import events.EventType;
import events.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestratorService {

    private final SagaOrderRepository sagaRepo;
    private final OutboxEventRepository outboxRepo;
    private final ProcessedEventRepository processedRepo;
    private final ObjectMapper mapper;

    @Transactional
    public void onEvent(EventEnvelope<OrderDTO> event) throws Exception {

        Long sagaId = Long.parseLong(event.getSagaId());

        SagaOrder saga =
                sagaRepo.findBySagaIdForUpdate(sagaId)
                        .orElseGet(() -> {
                            SagaOrder s = new SagaOrder();
                            s.setSagaId(sagaId);
                            s.setStatus(SagaStatus.IN_PROGRESS);
                            return sagaRepo.save(s);
                        });

        if (processedRepo.existsBySagaIdAndEventType(
                sagaId, event.getEventType())) {
            return;
        }

        switch (event.getEventType()) {

            case ORDER_CREATED -> {
                saga.setStep(SagaStep.ORDER_CREATED);
                emit(EventType.PAYMENT_RESERVE_REQUEST, event, "payment-commands");
            }

            case PAYMENT_ACCEPTED -> {
                saga.setPayment(ParticipantStatus.ACCEPT);
                saga.setStep(SagaStep.PAYMENT_RESERVED);
                emit(EventType.STOCK_RESERVE_REQUEST, event, "stock-commands");
            }

            case STOCK_ACCEPTED -> {
                saga.setStock(ParticipantStatus.ACCEPT);
                saga.setStatus(SagaStatus.CONFIRMED);
                saga.setStep(SagaStep.COMPLETED);

                emit(EventType.ORDER_CONFIRMED, event, "order-events");
                emit(EventType.PAYMENT_CONFIRM, event, "payment-commands");
                emit(EventType.STOCK_CONFIRM, event, "stock-commands");
            }

            case PAYMENT_REJECTED, STOCK_REJECTED -> {
                saga.setStatus(SagaStatus.ROLLBACK);
                saga.setStep(SagaStep.ROLLED_BACK);

                emit(EventType.ORDER_ROLLED_BACK, event, "order-events");
                emit(EventType.PAYMENT_COMPENSATE, event, "payment-commands");
                emit(EventType.STOCK_COMPENSATE, event, "stock-commands");
            }
        }

        processedRepo.save(
                new ProcessedEvent(null, sagaId, event.getEventType(), null)
        );

        sagaRepo.save(saga);
    }

    private void emit(EventType type, EventEnvelope<OrderDTO> src, String topic)
            throws Exception {

        Long sagaId = Long.parseLong(src.getSagaId());

        if (outboxRepo.existsByAggregateIdAndEventType(sagaId, type)) {
            return;
        }

        EventEnvelope<OrderDTO> out =
                new EventEnvelope<>(type, src.getSagaId(), "orchestrator", src.getPayload());

        OutboxEvent e = new OutboxEvent();
        e.setTopic(topic);
        e.setAggregateId(sagaId);
        e.setEventType(type);
        e.setPayload(mapper.writeValueAsString(out));

        outboxRepo.save(e);
    }
}
