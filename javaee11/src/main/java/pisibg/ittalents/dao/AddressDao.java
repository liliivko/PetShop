package pisibg.ittalents.dao;

import com.sun.org.apache.xalan.internal.xsltc.dom.AdaptiveResultTreeImpl;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Address;
import pisibg.ittalents.model.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressDao extends DAO {

    public Address saveAddress(Address address) throws SQLException {
        String sql = "INSERT INTO petshop.addresses (city, address_text, postal_code) " +
                "VALUES (?,?,?);";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, address.getCity());
            statement.setString(2, address.getAddress_text());
            statement.setString(3, address.getPostal_code());
            statement.executeUpdate();
            ResultSet addressID = statement.getGeneratedKeys();
            addressID.next();
            address.setId(addressID.getLong(1));
        }
        return address;
    }

    public void addAddressToBridgeTable(User user, Address address) throws SQLException {
        String sql = "INSERT INTO petshop.user_has_address (user_id, address_id) " +
                "VALUES (?,?);";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setLong(2, address.getId());
            statement.executeUpdate();
        }
    }

    public void deleteAddress(Address address) throws SQLException {
        String sql = "DELETE FROM addresses WHERE id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, address.getId());
            statement.executeUpdate();
        }
    }

    public void deleteUserAddress(User user) throws SQLException {
        String sql = "DELETE FROM user_has_address WHERE user_id= ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.executeUpdate();
        }
    }

    public void updateAddress(Address address) throws SQLException {
        String sql = "UPDATE addresses SET city=?, address_text=?, postal code=?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, address.getCity());
            statement.setString(2, address.getAddress_text());
            statement.setString(3, address.getPostal_code());
            statement.executeUpdate();
        }
    }



}


