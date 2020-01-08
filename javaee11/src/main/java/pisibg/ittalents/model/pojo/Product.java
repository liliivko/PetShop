package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.RegularPriceProductDTO;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Subcategory;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
@Entity
@Table(name = "products")
public class Product {

    //TODO mapping!

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    @OneToMany(mappedBy = "subcategory_id")
    private Subcategory subcategory;
    private LocalDate date;
    @OneToMany(mappedBy = "discount_id")
    private Discount discount;
    private double discounted_price;

    public Product(String name, double price, int quantity, String description, String image, Subcategory subcategory, LocalDate date) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.subcategory = subcategory;
        this.date = date;
    }

    public Product(String name, double price, int quantity, String description, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }

    public Product (RegularPriceProductDTO regularPriceProductDTO){
        setId(regularPriceProductDTO.getId());
        setName(regularPriceProductDTO.getName());
        setPrice(regularPriceProductDTO.getPrice());
        setQuantity(regularPriceProductDTO.getQuantity());
        setDescription(regularPriceProductDTO.getDescription());
        setImage(regularPriceProductDTO.getImage());
        setSubcategory(regularPriceProductDTO.getSubcategory());
        setDiscount(null);
        setDiscounted_price(0);
    }
}
