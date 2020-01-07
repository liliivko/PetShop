package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private int subcategory_id;
    private LocalDate date;
    private int discount_id;
    private double discounted_price;

    public Product(String name, double price, int quantity, String description, String image, int subcategory_id, LocalDate date) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.subcategory_id = subcategory_id;
        this.date = date;
    }
}
