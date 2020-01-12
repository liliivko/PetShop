package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "OrderProduct")
@Table(name = "order_has_product")
@Getter
@Setter
@NoArgsConstructor
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
        this.quantity = order.getOrderedProducts().get(product);
        this.id = new OrderProductId(order.getId(), product.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
