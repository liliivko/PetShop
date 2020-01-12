package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDao extends DAO {

    public void subscribe(long id) throws SQLException {
        String sql = "UPDATE users SET is_subscribed = 1 WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void unsubscribe(long id) throws SQLException {
        String sql = "UPDATE users SET is_subscribed = 0 WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void deleteUser(User user) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.executeUpdate();
        }
    }

    public boolean is_admin(User user) throws SQLException {
        String sql = "SELECT is_admin FROM petshop.users WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getBoolean("is_admin");
        }
    }
}
