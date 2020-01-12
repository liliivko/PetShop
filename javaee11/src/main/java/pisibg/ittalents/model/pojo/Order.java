package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import pisibg.ittalents.model.repository.AddressRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity=User.class)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    //TODO Address implementation in the order - JSON?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private LocalDateTime createdOn;
    @Transient
    private double totalPrice;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderProduct> products = new ArrayList<>();
    @Transient
    @Autowired
    AddressRepository addressRepository;

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

    public Order(User user, LocalDateTime createdOn) {
        this.user = user;
        this.createdOn = createdOn;
    }

    public Order(User user, HashMap<Product, Integer> cart, long paymentmethodId, Address address){
        this.user = user;
        setStatus(new Status(1));
        setPaymentMethod(new PaymentMethod(paymentmethodId));
        setAddress(address);
        setCreatedOn(LocalDateTime.now());
        setOrderedProducts(cart);
        setTotalPrice(orderPrice());
    }

    public void addProduct(Product product) {
        OrderProduct orderProduct = new OrderProduct(this, product);
        products.add(orderProduct);
        product.getOrders().add(orderProduct);
    }

    public void removeProduct(Product product) {
        for (Iterator<OrderProduct> iterator = products.iterator();
             iterator.hasNext(); ) {
            OrderProduct orderProduct = iterator.next();

            if (orderProduct.getOrder().equals(this) &&
                    orderProduct.getProduct().equals(product)) {
                iterator.remove();
                orderProduct.getProduct().getOrders().remove(getProducts());
                orderProduct.setOrder(null);
                orderProduct.setProduct(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
