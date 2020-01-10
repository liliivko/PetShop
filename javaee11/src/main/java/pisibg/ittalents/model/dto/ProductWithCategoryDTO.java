package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.Product;
import pisibg.ittalents.model.pojo.Subcategory;
import pisibg.ittalents.model.repository.CategoryRepository;

import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ProductWithCategoryDTO {

    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private String subcategoryName;
    private String categoryName;

    @Transient
    @JsonIgnore
    @Autowired
    CategoryRepository categoryRepository;

    public ProductWithCategoryDTO(Product product){
        setId(product.getId());
        setName(product.getName());
        setPrice(product.getPrice());
        setQuantity(product.getQuantity());
        setDescription(product.getDescription());
        setImage(product.getImage());
        setSubcategoryName(product.getSubcategory().getName());
        setCategoryName(product.getSubcategory().getCategory().getName());
    }


}
