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
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PaymentMethod")
@Table(name ="payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    public PaymentMethod(String type) {
        this.type = type;
    }
    PaymentMethod(long id){
        this.id = id;
    }
}
