package pl.piomin.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import events.EventEnvelope;
import events.EventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.piomin.order.OrderRepository.OrderRepository;
import pl.piomin.order.OrderRepository.OutboxEventRepository;
import pl.piomin.order.model.OrderEntity;
import pl.piomin.order.model.OutboxEvent;
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final OutboxEventRepository outboxRepo;
    private final ObjectMapper mapper;

    @PostMapping
    @Transactional
    public OrderEntity create(@RequestBody OrderDTO dto) throws Exception {

        OrderEntity order = new OrderEntity();
        order.setCustomerId(dto.getCustomerId());
        order.setProductId(dto.getProductId());
        order.setQuantity(dto.getQuantity());
        order.setPrice((int) dto.getPrice());

        orderRepo.save(order);

        // ✅ ORDER_CREATED → ORCHESTRATOR
        EventEnvelope<OrderDTO> event =
                new EventEnvelope<>(
                        EventType.ORDER_CREATED,
                        order.getId().toString(),
                        "order-service",
                        dto
                );

        OutboxEvent outbox = new OutboxEvent();
        outbox.setTopic("orders"); // ✅ CORRECT
        outbox.setAggregateId(order.getId());
        outbox.setEventType(EventType.ORDER_CREATED);
        outbox.setPayload(mapper.writeValueAsString(event));

        outboxRepo.save(outbox);

        return order;
    }
}
