package events;

public enum SagaStep {
    ORDER_CREATED,
    PAYMENT_RESERVED,
    STOCK_RESERVED,
    SHIPPING_CREATED,
    COMPLETED,
    ROLLED_BACK
}
