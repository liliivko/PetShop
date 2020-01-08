package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Entity
@Component
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private User user;
//    private Address address; -> or just address id?
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private LocalDateTime createdOn;
    @Transient
    private double totalPrice;

    @Transient
    private HashMap<Product, Integer> orderedProducts = new HashMap<>();

    @Transient
    private double orderPrice(){
        double price = 0;
        for (Map.Entry<Product, Integer> e : this.orderedProducts.entrySet()) {
            price+= e.getKey().getPrice()*e.getValue();
        }
        return price;
    }

    @Transient
    public int getNumberOfProducts() {
        return this.orderedProducts.size();
    }

    public Order(User user, LocalDateTime createdOn) {
        this.user = user;
        this.createdOn = createdOn;
    }

    public Order(Cart cart){
        setUser(cart.getUser());
        setStatus(new OrderStatus(1));
        setPaymentMethod(new PaymentMethod()); //where from, like the addresses?
        setCreatedOn(LocalDateTime.now());
        setOrderedProducts(cart.getItems());
        setTotalPrice(orderPrice());
    }

}
