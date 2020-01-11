package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findAllByNameLike(String name);
    List<Product> findAllByDiscountNotNull();
    List<Product> findAllByPriceBetween(double minPrice, double maxPrice);

    @Override
    void deleteById(Long aLong);


}

