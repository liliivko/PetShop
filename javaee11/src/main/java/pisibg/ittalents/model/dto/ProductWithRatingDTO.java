package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.Product;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ProductWithRatingDTO {

    private long id;
    private String name;
    private double price;
    private String image;
    private double stars;

    public ProductWithRatingDTO(Product product) throws SQLException {
        setId(product.getId());
        setName(product.getName());
        setPrice(setCurrentPrice(product));
        setImage(product.getImage());
    }

    private double setCurrentPrice(Product product) throws SQLException {
        if(product.getDiscount() != null &&
                LocalDateTime.now().isAfter(product.getDiscount().getDate_from())
                && LocalDateTime.now().isBefore(product.getDiscount().getDate_to())){
            return product.getPrice() - product.getDiscount().getAmount()/100*product.getPrice();
        }
        else {
            return product.getPrice();
        }
    }
}
