package pl.piomin.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piomin.stock.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
