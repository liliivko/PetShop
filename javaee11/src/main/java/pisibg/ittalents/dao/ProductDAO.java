package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProductDAO extends DAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;



}
