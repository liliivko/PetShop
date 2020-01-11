package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrderProductId implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_id")
    private Long orderId;

    private OrderProductId() {}

    public OrderProductId(
            Long productId,
            Long orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductId that = (OrderProductId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, orderId);
    }
}
