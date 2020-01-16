package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import pisibg.ittalents.model.repository.PaymentMethodRepository;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String type;

    public PaymentMethod(long id){
    this.setId(id);
}
    public PaymentMethod(String type) {
        this.type = type;
    }
}
