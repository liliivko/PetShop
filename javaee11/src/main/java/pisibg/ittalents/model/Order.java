package pisibg.ittalents.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long user_id;
    private long address_id;
    private String status;
    private long payment_method_id;
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private LocalDateTime created_on;
    @Transient
    private double totalPrice;
    @JsonManagedReference
    @OneToMany(mappedBy = "pk.order")
    @Transient
    private List<Product> orderedProducts = new ArrayList<>(); //for now?
    @Transient
    public double orderPrice(){
        double price = 0;
        for (Product p:this.orderedProducts) {
            price+= p.getPrice();
        }
        return price;
    }

    @Transient
    public int getNumberOfProducts() {
        return this.orderedProducts.size();
    }

}
