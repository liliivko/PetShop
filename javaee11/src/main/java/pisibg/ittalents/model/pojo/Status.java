package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "OrderStatus")
@Table(name ="order_statuses")
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String statusName;
//    @OneToMany(
//            mappedBy = "status",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//
//    private List<Order> orders = new ArrayList<>();

//    public void addOrder(Order order) {
//        orders.add(order);
//        order.setStatus(this);
//    }

    public Status(String statusName) {
        this.statusName = statusName;
    }

    public Status(long id){
        this.id = id;
    }
}
