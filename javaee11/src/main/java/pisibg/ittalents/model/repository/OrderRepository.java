package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {

}
