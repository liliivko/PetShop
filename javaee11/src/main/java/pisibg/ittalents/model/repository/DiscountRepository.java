package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Discount;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
