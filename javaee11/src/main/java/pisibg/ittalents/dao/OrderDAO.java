package pisibg.ittalents.dao;

import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.OrdersByUserDTO;
import pisibg.ittalents.model.dto.ProductFromCartDTO;
import pisibg.ittalents.model.pojo.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDAO extends DAO {

    //Calculated in the DB the total price of the order as temporary extra column for the query,
    //based on the products and if they have been discounted by the time the order has been made.
    private static final String ORDERS_BY_USER_SQL = "SELECT o.id AS order_id, o.created_on AS order_date,\n" +
            "            o.user_id, a.address_text, os.name, pm.type, o.date,\n" +
            "            SUM(IF((p.discount_id is not null \n" +
            "            AND d.date_from<o.created_on\n" +
            "            AND d.date_to>o.created_on), p.price*(1-d.amount/100)*op.quantity,p.price*op.quantity))\n" +
            "            AS total_price, p.name, pm.id, os.id\n" +
            "            FROM orders AS o\n" +
            "            JOIN addresses AS a\n" +
            "            ON o.address_id = a.id\n" +
            "            JOIN order_statuses AS os\n" +
            "            ON o.status_id = os.id\n" +
            "            JOIN payment_methods AS pm \n" +
            "            ON o.payment_method_id = pm.id\n" +
            "            JOIN order_has_product AS op\n" +
            "            ON op.order_id = o.id\n" +
            "            JOIN products as p\n" +
            "            ON op.product_id = p.id\n" +
            "            LEFT OUTER JOIN discounts as d\n" +
            "            ON d.id = p.discount_id\n" +
            "            WHERE o.user_id = ?\n" +
            "            GROUP BY o.id;";

    private static final String PRODUCTS_FROM_ORDER_SQL = "SELECT o.date as date, o.id as order_id, p.id as product_id, p.name, \n" +
            "IF((p.discount_id is not null AND d.date_from<o.created_on \n" +
            "AND d.date_to>o.created_on), p.price*(1-d.amount/100) ,p.price)\n" +
            "AS price, op.quantity, p.description, s.name as subcategory, c.name, s.id, c.id\n" +
            "FROM orders AS o\n" +
            "JOIN order_has_product AS op \n" +
            "ON order_id = o.id \n" +
            "JOIN products AS p \n" +
            "ON product_id = p.id\n" +
            "JOIN subcategories as s\n" +
            "ON s.id = p.subcategory_id\n" +
            "JOIN categories as c\n" +
            "ON c.id = s.category_id\n" +
            "LEFT OUTER JOIN discounts AS d\n" +
            "ON d.id = p.discount_id\n" +
            "WHERE order_id = ?\n" +
            "GROUP BY product_id";


    public List<ProductFromCartDTO> getProductsFromOrder (Order order) throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<ProductFromCartDTO> products = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(PRODUCTS_FROM_ORDER_SQL)) {
            ps.setLong(1, order.getId());
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                ProductFromCartDTO product = new ProductFromCartDTO();
                product.setId(rows.getLong("product_id"));
                product.setQuantity(rows.getInt("op.quantity"));
                product.setName(rows.getString("p.name"));
                double price = rows.getDouble("price");
                DecimalFormat df = new DecimalFormat("#.##");
                price = Double.parseDouble(df.format(price));
                product.setPrice(price);
                product.setDescription(rows.getString("description"));
                product.setCategoryName(rows.getString("c.name"));
                product.setSubcategoryName(rows.getString("subcategory"));
                products.add(product);
            }
        }
        return products;
    }

    public List<OrdersByUserDTO> getAllOrdersByUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<OrdersByUserDTO> orders = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(ORDERS_BY_USER_SQL)) {
            ps.setLong(1, user.getId());
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                OrdersByUserDTO order = new OrdersByUserDTO();
                order.setId(rows.getLong("order_id"));
                order.setPaymentMethod(new PaymentMethod(rows.getInt("pm.id"), rows.getString("pm.type")));
                //when calculating the price in the DB, it returns 10 decimal places, so we are rounding it to 2.
                double price = rows.getDouble("total_price");
                DecimalFormat df = new DecimalFormat("#.##");
                price = Double.parseDouble(df.format(price));
                order.setTotalPrice(price);
                order.setCreatedOn(rows.getTimestamp("date").toLocalDateTime());
                order.setStatus(new Status(rows.getInt("os.id"),rows.getString("os.name")));
                orders.add(order);
            }
        }
        return orders;
    }
}
