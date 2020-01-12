package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Category;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Subcategory;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RegularPriceProductDTO {

    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private long subcategoryId;
    @JsonIgnore
    private Subcategory subcategory;

    public RegularPriceProductDTO(Product product){
        setId(product.getId());
     setName(product.getName());
     setPrice(product.getPrice());
     setQuantity(product.getQuantity());
     setDescription(product.getDescription());
     setImage(product.getImage());
     setSubcategoryId(product.getSubcategory().getId());
    }
}
