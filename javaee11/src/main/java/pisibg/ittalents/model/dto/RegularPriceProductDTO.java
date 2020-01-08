package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Subcategory;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Component
@Entity
@Table(name = "products")
public class RegularPriceProductDTO {

    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private Subcategory subcategory;

    public RegularPriceProductDTO(Product product){
        setId(product.getId());
     setName(product.getName());
     setPrice(product.getPrice());
     setQuantity(product.getQuantity());
     setDescription(product.getDescription());
     setImage(product.getImage());
     setSubcategory(product.getSubcategory());
    }

}
