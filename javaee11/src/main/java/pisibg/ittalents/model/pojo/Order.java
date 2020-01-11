package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "address_id")
//    private Address address;
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private LocalDateTime createdOn;
    @Transient
    private double totalPrice;

//    @ManyToMany(cascade = { CascadeType.ALL })
//    @JoinTable(
//            name = "order_has_product",
//            joinColumns = { @JoinColumn(name = "order_id") },
//            inverseJoinColumns = { @JoinColumn(name = "product_id") }
//    )
//    Set<Product> products = new HashSet<>();

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderProduct> products = new ArrayList<>();
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
        this.user = user;
        setStatus(new Status(1));
        setPaymentMethod(new PaymentMethod(1));
        setCreatedOn(LocalDateTime.now());
        setOrderedProducts(cart);
        setTotalPrice(orderPrice());
    }


    public void addProduct(Product product) {
        OrderProduct orderProduct = new OrderProduct(this, product);
        products.add(orderProduct);
        product.getOrders().add(orderProduct);
    }

    public void removeTag(Product product) {
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
