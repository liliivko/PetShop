package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserDao extends DAO {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public void subscribe(User u) throws SQLException {
        String sql = "UPDATE users SET subscribed = ? WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, u.getId());
            statement.setBoolean(2, true);
            statement.executeUpdate();
        }
    }

    public void unsubscribe(User u) throws SQLException {
        String sql = "UPDATE users SET subscribed = ? WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, u.getId());
            statement.setBoolean(2, false);
            statement.executeUpdate();
        }
    }


}
