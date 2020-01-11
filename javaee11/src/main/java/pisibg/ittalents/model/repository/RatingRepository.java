package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Rating;

import java.util.List;

@Repository

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByUser_Id(long id);

}
