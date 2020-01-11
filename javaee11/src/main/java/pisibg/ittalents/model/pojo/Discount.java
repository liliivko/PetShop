package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double amount;
    private LocalDateTime expiryDate;

}
