package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class Discount {
    private long id;
    private String name;
    private double amount;
    private LocalDateTime expiryDate;
}
