package pl.piomin.stock.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import events.EventEnvelope;
import events.EventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.piomin.stock.domain.ProcessedOrder;
import pl.piomin.stock.domain.Product;
import pl.piomin.stock.repository.ProcessedOrderRepository;
import pl.piomin.stock.repository.ProductRepository;
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepo;
    private final ProcessedOrderRepository processedRepo;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;

    @Transactional
    public void handle(EventEnvelope<OrderDTO> event) throws Exception {

        Long sagaId = Long.parseLong(event.getSagaId());

        if (processedRepo.existsBySagaIdAndEventType(sagaId, event.getEventType())) {
            return;
        }

        switch (event.getEventType()) {
            case STOCK_RESERVE_REQUEST -> reserve(event);
            case STOCK_CONFIRM -> confirm(event);
            case STOCK_COMPENSATE -> rollback(event);
        }

        processedRepo.save(new ProcessedOrder(sagaId, event.getEventType()));
    }

    private void reserve(EventEnvelope<OrderDTO> event) throws Exception {

        Product p = productRepo.findById(event.getPayload().getProductId())
                .orElse(null);

        if (p == null) {
            emit(EventType.STOCK_REJECTED, event);
            return;
        }

        try {
            p.reserve(event.getPayload().getQuantity());
            productRepo.save(p);
            emit(EventType.STOCK_ACCEPTED, event);
        } catch (Exception e) {
            emit(EventType.STOCK_REJECTED, event);
        }
    }

    private void confirm(EventEnvelope<OrderDTO> event) {
        productRepo.findById(event.getPayload().getProductId())
                .ifPresent(p -> p.confirm(event.getPayload().getQuantity()));
    }

    private void rollback(EventEnvelope<OrderDTO> event) {
        productRepo.findById(event.getPayload().getProductId())
                .ifPresent(p -> p.rollback(event.getPayload().getQuantity()));
    }

    private void emit(EventType type, EventEnvelope<OrderDTO> src) throws Exception {
        kafka.send(
                "stock-events",
                src.getSagaId(),
                mapper.writeValueAsString(
                        new EventEnvelope<>(type, src.getSagaId(), "stock", src.getPayload())
                )
        );
    }
}
