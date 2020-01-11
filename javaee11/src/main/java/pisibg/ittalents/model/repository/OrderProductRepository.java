package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.Product;

@Repository
public interface OrderProductRepository extends JpaRepository<Order, Product> {
}
