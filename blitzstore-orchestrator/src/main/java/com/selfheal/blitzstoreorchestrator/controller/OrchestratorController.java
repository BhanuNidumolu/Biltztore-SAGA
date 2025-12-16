package com.selfheal.blitzstoreorchestrator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfheal.blitzstoreorchestrator.domain.OutboxEvent;
import com.selfheal.blitzstoreorchestrator.domain.OutboxStatus;
import com.selfheal.blitzstoreorchestrator.domain.SagaOrder;
import com.selfheal.blitzstoreorchestrator.domain.SagaStatus;
import com.selfheal.blitzstoreorchestrator.repository.OutboxEventRepository;
import com.selfheal.blitzstoreorchestrator.repository.SagaOrderRepository;
import dto.OrderDTO;
import events.EventEnvelope;
import events.EventType;
import events.SagaStep;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orchestrator")
@RequiredArgsConstructor
public class OrchestratorController {

    private final OutboxEventRepository outboxRepo;
    private final SagaOrderRepository sagaRepo;
    private final ObjectMapper mapper;

    /**
     * 🚀 START SAGA
     */
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public SagaOrder startSaga(@RequestBody OrderDTO order) throws Exception {

        String sagaId = UUID.randomUUID().toString();

        // 1️⃣ Create saga entry
        SagaOrder saga = new SagaOrder();
        saga.setSagaId(Long.parseLong(sagaId));
        saga.setStep(SagaStep.ORDER_CREATED);
        saga.setStatus(SagaStatus.IN_PROGRESS);
        sagaRepo.save(saga);

        // 2️⃣ Create ORDER_CREATED event
        EventEnvelope<OrderDTO> event =
                new EventEnvelope<>(
                        EventType.ORDER_CREATED,
                        sagaId,
                        "orchestrator",
                        order
                );

        // 3️⃣ Store event in outbox
        OutboxEvent outbox = new OutboxEvent();
        outbox.setTopic("orders");
        outbox.setPayload(mapper.writeValueAsString(event));
        outbox.setStatus(OutboxStatus.NEW);

        outboxRepo.save(outbox);

        return saga;
    }

    /**
     * 📜 VIEW ALL SAGAS
     */
    @GetMapping("/sagas")
    public List<SagaOrder> allSagas() {
        return sagaRepo.findAll();
    }
}
