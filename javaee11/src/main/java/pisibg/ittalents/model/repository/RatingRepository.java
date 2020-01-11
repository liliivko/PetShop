package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.ittalents.model.pojo.Rating;

public interface RatingRepository extends JpaRepository<Rating,Long> {
}
