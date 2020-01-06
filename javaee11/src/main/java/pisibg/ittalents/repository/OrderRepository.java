package pisibg.ittalents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.ittalents.model.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {
}
