package pisibg.ittalents.dao;

import org.springframework.stereotype.Component;
import pisibg.ittalents.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao extends DAO {

    public List<User> getAllUsers(){
        
        List<User> users = jdbcTemplate.query("SELECT id, first_name, last_name, gender, email FROM users", (resultSet, i)-> toUser(resultSet));
        return users;
    }

    private User toUser(ResultSet resultSet) throws SQLException {
        User u = new User();
        u.setId(resultSet.getLong("id"));
        u.setFirstName(resultSet.getString("first_name"));
        u.setLastName(resultSet.getString("last_name"));
        u.setGender(resultSet.getString("gender"));
        u.setEmail(resultSet.getString("email"));
        return u;
    }
}
