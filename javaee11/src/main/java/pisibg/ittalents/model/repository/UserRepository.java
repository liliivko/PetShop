package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pisibg.ittalents.model.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);


}
