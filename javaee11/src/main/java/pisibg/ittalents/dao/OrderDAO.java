package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.ittalents.model.pojo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO extends DAO {

    private static final String ORDERS_BY_USER_SQL = "SELECT o.id as order_id, o.user_id, a.address_text, os.name, pm.type, date \n" +
            "FROM orders AS o\n" +
            "JOIN addresses AS a\n" +
            "ON o.address_id = a.id\n" +
            "JOIN order_statuses AS os\n" +
            "ON o.status_id = os.id\n" +
            "JOIN payment_methods AS pm \n" +
            "ON o.payment_method_id = pm.id" +
            "WHERE user_id = ?";


    private static final String PRODUCTS_FROM_ORDER_SQL = "SELECT o.id as order_id, p.id as product_id, p.name, p.price, op.quantity, p.description, p.image, s.name as subcategory, c.name, s.id, c.id\n" +
            "            FROM orders AS o\n" +
            "            JOIN order_has_product AS op \n" +
            "            ON order_id = o.id \n" +
            "            JOIN products AS p \n" +
            "            ON product_id = p.id\n" +
            "            JOIN subcategories as s\n" +
            "            ON s.id = p.subcategory_id\n" +
            "            JOIN categories as c\n" +
            "            ON c.id = s.category_id\n" +
            "            WHERE order_id = ? \n" +
            "            GROUP BY product_id";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HashMap<Product, Integer> getProductsFromOrder (Order order) throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();

        try(PreparedStatement ps = connection.prepareStatement(PRODUCTS_FROM_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getId());
            ResultSet rows = ps.executeQuery();
            if(rows.next()) {
                Product product = new Product(rows.getString("p.name"),
                        rows.getDouble("p.price"),
                        rows.getInt("op.quantity"),
                        rows.getString("p.description"),
                        rows.getString("p.image"),
                        new Subcategory(rows.getLong ("p.subcategory_id"),
                                rows.getString("s.name"),
                                new Category(rows.getLong("c.id"),
                                            rows.getString("c.name"))),
                        rows.getDate("date").toLocalDate()
                        );
                order.getOrderedProducts().put(product, product.getQuantity());
            }
            else{
                return null;
            }
        }
        return order.getOrderedProducts();
    }

    public List<Order> getAllOrdersByUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<Order> orders = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(ORDERS_BY_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, user.getId());
            ResultSet rows = ps.executeQuery();
            if(rows.next()) {
                //TODO to add statuses and payment methods!!! - value from enum form DB? is it even necessary... total Price also!
                Order order = new Order(user, rows.getTimestamp ("date").toLocalDateTime());
                order.setOrderedProducts(getProductsFromOrder (order));
                order.setTotalPrice(order.getTotalPrice());
                orders.add(order);
            }
            else{
                return null;
            }
        }
        return orders;
    }


}
