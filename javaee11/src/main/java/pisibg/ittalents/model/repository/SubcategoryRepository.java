package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Category;
import pisibg.ittalents.model.pojo.Subcategory;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findAllByCategory(Category category);
}
