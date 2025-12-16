package com.selfheal.blitzstoreorchestrator.repository;

import com.selfheal.blitzstoreorchestrator.domain.SagaOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SagaOrderRepository
        extends JpaRepository<SagaOrder, Long> {

    /**
     * 🔒 Critical: ensures only ONE saga instance
     * is modified at a time (Kafka retries / rebalances safe)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SagaOrder s where s.sagaId = :sagaId")
    Optional<SagaOrder> findBySagaIdForUpdate(
            @Param("sagaId") Long sagaId
    );
}
