package pl.piomin.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import events.EventEnvelope;
import events.EventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.piomin.payment.domain.Customer;
import pl.piomin.payment.domain.ProcessedOrder;
import pl.piomin.payment.repository.CustomerRepository;
import pl.piomin.payment.repository.ProcessedOrderRepository;
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CustomerRepository customerRepo;
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
            case PAYMENT_RESERVE_REQUEST -> reserve(event);
            case PAYMENT_CONFIRM -> confirm(event);
            case PAYMENT_COMPENSATE -> rollback(event);
        }

        processedRepo.save(new ProcessedOrder(sagaId, event.getEventType()));
    }

    private void reserve(EventEnvelope<OrderDTO> event) throws Exception {

        Customer c = customerRepo.findById(event.getPayload().getCustomerId())
                .orElse(null);

        if (c == null) {
            emit(EventType.PAYMENT_REJECTED, event);
            return;
        }

        try {
            c.reserve(event.getPayload().getPrice());
            customerRepo.save(c);
            emit(EventType.PAYMENT_ACCEPTED, event);
        } catch (Exception e) {
            emit(EventType.PAYMENT_REJECTED, event);
        }
    }

    private void confirm(EventEnvelope<OrderDTO> event) {
        customerRepo.findById(event.getPayload().getCustomerId())
                .ifPresent(c -> c.confirm(event.getPayload().getPrice()));
    }

    private void rollback(EventEnvelope<OrderDTO> event) {
        customerRepo.findById(event.getPayload().getCustomerId())
                .ifPresent(c -> c.rollback(event.getPayload().getPrice()));
    }

    private void emit(EventType type, EventEnvelope<OrderDTO> src) throws Exception {
        kafka.send(
                "payment-events",
                src.getSagaId(),
                mapper.writeValueAsString(
                        new EventEnvelope<>(type, src.getSagaId(), "payment", src.getPayload())
                )
        );
    }
}
