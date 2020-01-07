package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.ittalents.model.pojo.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findAllByName(String name);

    @Override
    void deleteById(Long aLong);
}

