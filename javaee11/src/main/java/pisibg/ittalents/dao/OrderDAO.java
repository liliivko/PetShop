package pisibg.ittalents.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.OrdersByUserDTO;
import pisibg.ittalents.model.dto.ProductFromCartDTO;
import pisibg.ittalents.model.pojo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDAO extends DAO {

    private static final String ORDERS_BY_USER_SQL = "SELECT o.id as order_id, o.user_id, a.address_text, os.name, pm.type, o.date, Sum(op.quantity*p.price) as total_price\n" +
            "            FROM orders AS o\n" +
            "            LEFT OUTER JOIN addresses AS a\n" +
            "            ON o.address_id = a.id\n" +
            "            JOIN order_statuses AS os\n" +
            "            ON o.status_id = os.id\n" +
            "            JOIN payment_methods AS pm \n" +
            "            ON o.payment_method_id = pm.id\n" +
            "            LEFT OUTER JOIN order_has_product AS op\n" +
            "            ON op.order_id = o.id\n" +
            "            LEFT OUTER JOIN products as p\n" +
            "            ON op.product_id = p.id\n" +
            "            WHERE o.user_id = ?\n" +
            "            group by o.id";

    private static final String PRODUCTS_FROM_ORDER_SQL = "SELECT o.date as date, o.id as order_id, p.id as product_id, p.name, p.price, op.quantity, p.description, p.image, s.name as subcategory, c.name, s.id, c.id\n" +
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

// TODO remove generated keys
    public List<ProductFromCartDTO> getProductsFromOrder (Order order) throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<ProductFromCartDTO> products = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(PRODUCTS_FROM_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getId());
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                ProductFromCartDTO product = new ProductFromCartDTO();
                product.setId(rows.getLong("product_id"));
               product.setQuantity(rows.getInt("quantity"));
                product.setName(rows.getString("p.name"));
                product.setPrice(rows.getDouble("price"));
                product.setDescription(rows.getString("description"));
                product.setCategoryName(rows.getString("c.name"));
                product.setSubcategoryName(rows.getString("subcategory"));
                product.setImage(rows.getString("image"));
                products.add(product);
            }
        }
        return products;
    }

    public List<OrdersByUserDTO> getAllOrdersByUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<OrdersByUserDTO> orders = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(ORDERS_BY_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, user.getId());
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                OrdersByUserDTO order = new OrdersByUserDTO();
                order.setId(rows.getLong("order_id"));
                order.setPaymentMethod(new PaymentMethod(rows.getString("type")));
                order.setTotalPrice(rows.getDouble("total_price"));
                order.setCreatedOn(rows.getTimestamp("date").toLocalDateTime());
                order.setStatus(new Status(rows.getString("name")));
                orders.add(order);
            }
        }
        return orders;
    }
}
