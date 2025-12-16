package pl.piomin.order.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import events.EventEnvelope;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.piomin.order.OrderRepository.OrderRepository;
import pl.piomin.order.model.OrderEntity;
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderRepository orderRepo;
    private final ObjectMapper mapper;

    // 🔥 ONLY listen to order-events
    @KafkaListener(topics = "order-events", groupId = "order")
    @Transactional
    public void onMessage(String payload) throws Exception {

        EventEnvelope<OrderDTO> event =
                mapper.readValue(payload,
                        new TypeReference<EventEnvelope<OrderDTO>>() {});

        Long orderId = Long.parseLong(event.getSagaId());

        OrderEntity order =
                orderRepo.findByIdForUpdate(orderId)
                        .orElseThrow();

        log.info("🧾 Order received {} sagaId={}",
                event.getEventType(), event.getSagaId());

        switch (event.getEventType()) {

            case ORDER_CONFIRMED -> order.confirm();

            case ORDER_ROLLED_BACK -> order.rollback();

            default -> {
                // ignore everything else
            }
        }
    }
}
