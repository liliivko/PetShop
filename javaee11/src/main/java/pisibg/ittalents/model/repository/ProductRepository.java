package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Category;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Subcategory;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findAllByNameLike(String name);
    List<Product> findAllByDiscountNotNull();
    List<Product> findAllByPriceBetween(double minPrice, double maxPrice);
    List<Product>findAllByDiscountId(long discount_id);
    List <Product> findAllBySubcategory(Subcategory subcategory);
    boolean existsByName(String name);
}

