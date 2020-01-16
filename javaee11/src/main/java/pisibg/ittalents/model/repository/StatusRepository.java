package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Rating;
import pisibg.ittalents.model.pojo.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
