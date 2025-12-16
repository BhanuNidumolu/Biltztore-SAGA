package pl.piomin.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long productId;
    private int quantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    @Version
    private Long version;

    public void confirm() {
        if (status == OrderStatus.NEW) {
            status = OrderStatus.CONFIRMED;
        }
    }

    public void rollback() {
        if (status == OrderStatus.NEW) {
            status = OrderStatus.ROLLED_BACK;
        }
    }
}
