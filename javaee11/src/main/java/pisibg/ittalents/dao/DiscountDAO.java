package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.OrdersByUserDTO;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.PaymentMethod;
import pisibg.ittalents.model.pojo.Status;
import pisibg.ittalents.model.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DiscountDAO extends DAO {

    private static final String ORDERS_BY_USER_SQL = "UPDATE products as p" +
            " JOIN subcategories " +
            "s on p.subcategory_id = s.id SET " +
            "discount_id=? WHERE subcategory_id =? ; ";

    public void applyDiscount(long discountId,long subcategoryId) throws SQLException {
        String sql = ORDERS_BY_USER_SQL;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, discountId);
            statement.setLong(2,subcategoryId);
            statement.executeUpdate();
        }
    }

}
