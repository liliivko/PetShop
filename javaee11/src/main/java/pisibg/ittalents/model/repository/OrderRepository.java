package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.ittalents.model.pojo.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {
}
