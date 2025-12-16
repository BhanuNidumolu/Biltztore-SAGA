package events;

public enum EventType {

    // ===== Order lifecycle (Orchestrator only) =====
    ORDER_CREATED,
    ORDER_CONFIRMED,
    ORDER_ROLLED_BACK,

    // ===== Payment commands & replies =====
    PAYMENT_RESERVE_REQUEST,
    PAYMENT_CONFIRM,
    PAYMENT_COMPENSATE,
    PAYMENT_ACCEPTED,
    PAYMENT_REJECTED,

    // ===== Stock commands & replies =====
    STOCK_RESERVE_REQUEST,
    STOCK_CONFIRM,
    STOCK_COMPENSATE,
    STOCK_ACCEPTED,
    STOCK_REJECTED,

    // ===== Shipping commands & replies =====
    SHIPPING_CREATE_REQUEST,
    SHIPPING_CANCEL,
    SHIPPING_CREATED
}
