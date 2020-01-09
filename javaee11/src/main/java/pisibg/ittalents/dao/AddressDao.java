package pisibg.ittalents.dao;

import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class AddressDao extends DAO {

    public void saveAddress(Address address) throws SQLException {
        String sql = "INSERT INTO petshop.addresses (city, address_text, postal_code) " +
                "VALUES (?,?,?);";
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address.getCity());
            statement.setString(2, address.getAddress_text());
            statement.setString(3, address.getPostal_code());
            statement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Oops");
        }
    }

    public void addAddress(User user, Address address) throws SQLException {
        String sql = "INSERT INTO petshop.user_has_address (user_id, address_id) " +
                "VALUES (?,?);";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, address.getId());
            statement.executeUpdate();
        }
    }


}
