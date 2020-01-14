package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import pisibg.ittalents.dao.DiscountDAO;
import pisibg.ittalents.exception.NotFoundException;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.repository.DiscountRepository;

import javax.persistence.Transient;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ProductWithCurrentPriceDTO {

    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private String subcategoryName;
    private String categoryName;
    private Discount discount;



    public ProductWithCurrentPriceDTO(Product product) throws SQLException {
        setId(product.getId());
        setName(product.getName());
        setPrice(setCurrentPrice(product));
        setQuantity(product.getQuantity());
        setDescription(product.getDescription());
        setImage(product.getImage());
        setSubcategoryName(product.getSubcategory().getName());
        setCategoryName(product.getSubcategory().getCategory().getName());
        setDiscount(product.getDiscount());
    }

    public ProductWithCurrentPriceDTO(Product product, int quantity) throws SQLException {
        setId(product.getId());
        setName(product.getName());
        setPrice(setCurrentPrice(product));
        setQuantity(quantity);
        setDescription(product.getDescription());
        setImage(product.getImage());
        setSubcategoryName(product.getSubcategory().getName());
        setCategoryName(product.getSubcategory().getCategory().getName());
        setDiscount(product.getDiscount());
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
