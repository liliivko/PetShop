package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Order;
import pisibg.ittalents.model.pojo.Product;
import java.sql.SQLException;
@Getter
@Setter
@NoArgsConstructor
@Component

public class ProductWithOrderPriceDTO {
    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private String subcategoryName;
    private String categoryName;
    private Discount discount;

    public ProductWithOrderPriceDTO(Product product, Order order) throws SQLException {
        setId(product.getId());
        setName(product.getName());
        setPrice(setPriceWhenOrdered(product, order));
        setQuantity(product.getQuantity());
        setDescription(product.getDescription());
        setSubcategoryName(product.getSubcategory().getName());
        setCategoryName(product.getSubcategory().getCategory().getName());
        setDiscount(setDiscountWhenOrdered(product, order));
        setImage(product.getImage());
    }

    private double setPriceWhenOrdered(Product product, Order order) throws SQLException {
        if(product.getDiscount() != null
                && order.getCreatedOn().isAfter(product.getDiscount().getDate_from())
                && order.getCreatedOn().isBefore(product.getDiscount().getDate_to())){
            return product.getPrice() - product.getDiscount().getAmount()/100*product.getPrice();
        }
        else {
            return product.getPrice();
        }
    }

    private Discount setDiscountWhenOrdered(Product product, Order order) throws SQLException {
        if(product.getDiscount() != null
                && order.getCreatedOn().isAfter(product.getDiscount().getDate_from())
                && order.getCreatedOn().isBefore(product.getDiscount().getDate_to())){
            return product.getDiscount();
        }
        else {
            return null;
        }
    }
}
