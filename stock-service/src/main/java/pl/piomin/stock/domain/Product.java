package pl.piomin.stock.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int available;
    private int reserved;

    public void reserve(int qty) {
        if (qty > available) {
            throw new IllegalStateException("Insufficient stock");
        }
        available -= qty;
        reserved += qty;
    }

    public void confirm(int qty) {
        if (reserved < qty) return;
        reserved -= qty;
    }

    public void rollback(int qty) {
        if (reserved < qty) return;
        reserved -= qty;
        available += qty;
    }
}
