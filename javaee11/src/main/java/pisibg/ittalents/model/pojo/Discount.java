package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.DiscountDTO;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity
@Table(name="discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "This field should not be null")
    private String name;
    @NotNull
    private double amount;
    @Future
    private LocalDateTime date_from;
    @Future
    private LocalDateTime date_to;

    public Discount(DiscountDTO dto){
        setName(dto.getName());
        setAmount(dto.getAmount());
        setDate_from(dto.getDate_from());
        setDate_to(dto.getDate_to());
    }
}
