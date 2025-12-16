package com.selfheal.blitzstoreorchestrator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import events.EventEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class OrchestratorEventListener {

    private final OrchestratorService service;
    private final ObjectMapper mapper;

    @KafkaListener(
            topics = {"orders", "payment-events", "stock-events"},
            groupId = "orchestrator"
    )
    public void onMessage(String payload) throws Exception {

        EventEnvelope<OrderDTO> event =
                mapper.readValue(payload, new TypeReference<>() {});

        log.info("📥 Orchestrator received {} sagaId={} from {}",
                event.getEventType(),
                event.getSagaId(),
                event.getSource());

        service.onEvent(event);
    }
}
