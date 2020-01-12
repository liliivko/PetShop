package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.RegularPriceProductDTO;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.Subcategory;
import pisibg.ittalents.model.repository.SubcategoryRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Component
@Entity
@Table(name = "products")
@NaturalIdCache
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NaturalId
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private Discount discount;
    private double discountedPrice;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderProduct> orders = new ArrayList<>();

    public Product(String name, double price, int quantity, String description, String image, Subcategory subcategory, LocalDate date) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.subcategory = subcategory;
        this.date = date;
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
        setDiscountedPrice(0);
        setDate(LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Double.compare(product.price, price) == 0 &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(image, product.image) &&
                Objects.equals(subcategory, product.subcategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, image, subcategory);
    }
}
