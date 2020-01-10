package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Entity(name = "Order")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
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

    public Order(User user, HashMap<Product, Integer> cart){
        setUser(user);
        setStatus(new Status(1));
        setPaymentMethod(new PaymentMethod());
        setCreatedOn(LocalDateTime.now());
        setOrderedProducts(cart);
        setTotalPrice(orderPrice());
    }

}
