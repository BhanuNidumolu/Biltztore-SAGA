package pl.piomin.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private long amountAvailable;
    private long amountReserved;

    public void reserve(long amount) {
        if (amount > amountAvailable) {
            throw new IllegalStateException("Insufficient funds");
        }
        amountAvailable -= amount;
        amountReserved += amount;
    }

    public void confirm(long amount) {
        if (amountReserved < amount) return;
        amountReserved -= amount;
    }

    public void rollback(long amount) {
        if (amountReserved < amount) return;
        amountReserved -= amount;
        amountAvailable += amount;
    }
}
