package dto;

public class OrderDTO {

    private Long orderId;
    private Long customerId;
    private Long productId;
    private int quantity;
    private long price; // paise

    public OrderDTO() {}

    public OrderDTO(Long orderId, Long customerId, Long productId, int quantity, long price) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public long getPrice() { return price; }
}
