package pisibg.ittalents.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.ittalents.model.pojo.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
