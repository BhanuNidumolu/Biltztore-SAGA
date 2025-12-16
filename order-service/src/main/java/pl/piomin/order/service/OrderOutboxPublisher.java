package pl.piomin.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.piomin.order.OrderRepository.OutboxEventRepository;
import pl.piomin.order.model.OutboxEvent;
import pl.piomin.order.model.OutboxStatus;

import java.util.List;
@Component
@RequiredArgsConstructor
public class OrderOutboxPublisher {

    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, String> kafka;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publish() {

        List<OutboxEvent> events =
                outboxRepo.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);

        for (OutboxEvent e : events) {
            try {
                kafka.send(
                        e.getTopic(),
                        e.getAggregateId().toString(),
                        e.getPayload()
                ).get();

                e.setStatus(OutboxStatus.SENT);

            } catch (Exception ex) {
                e.setRetryCount(e.getRetryCount() + 1);
                e.setLastError(ex.getMessage());
                if (e.getRetryCount() >= 5) {
                    e.setStatus(OutboxStatus.FAILED);
                }
            }
            outboxRepo.save(e);
        }
    }
}
