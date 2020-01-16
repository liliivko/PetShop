package pisibg.ittalents.dao;

import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.ProductWithRatingDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class RatingDAO extends DAO{

    private static final String PRODUCTS_WITH_STARS = "SELECT p.id, p.name, \n" +
            "(IF((p.discount_id is not null \n" +
            "AND d.date_from<now()\n" +
            "AND d.date_to>now()), p.price*(1-d.amount/100),p.price)) \n" +
            "AS price,\n" +
            "image, SUM(rating_stars)/count(rating_stars) as rating_stars FROM products as p\n" +
            "JOIN ratings as r\n" +
            "ON r.product_id = p.id\n" +
            "JOIN discounts as d\n" +
            "ON d.id = p.discount_id\n" +
            "GROUP BY p.id;\n";


    public List<ProductWithRatingDTO> getProductsByRating() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<ProductWithRatingDTO> productsWithRatings = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(PRODUCTS_WITH_STARS)) {
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                    ProductWithRatingDTO product = new ProductWithRatingDTO();
                    product.setId(rows.getLong("p.id"));
                    product.setName(rows.getString("p.name"));
                    double price = rows.getDouble("price");
                    DecimalFormat df = new DecimalFormat("#.##");
                    price = Double.parseDouble(df.format(price));
                    product.setPrice(price);
                    product.setStars(rows.getDouble("rating_stars"));
                    productsWithRatings.add(product);
            }
        }
        productsWithRatings.sort((o1,o2) -> Double.compare(o2.getStars(), o1.getStars()));
        return productsWithRatings;
    }
}
