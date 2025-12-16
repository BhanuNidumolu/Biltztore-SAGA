package pl.piomin.payment.service;

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
public class PaymentEventListener {

    private final PaymentService service;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "payment-commands", groupId = "payment")
    public void onMessage(String payload) throws Exception {

        EventEnvelope<OrderDTO> event =
                mapper.readValue(payload, new TypeReference<>() {});

        log.info("💳 Payment received {} sagaId={}",
                event.getEventType(), event.getSagaId());

        service.handle(event);
    }
}
