package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RatingDao extends DAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

}
