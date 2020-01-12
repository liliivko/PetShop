package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

}
