package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class DiscountDTO {
    private long id;
    private String name;
    private double amount;
    private LocalDateTime date_from;
    private LocalDateTime date_to;
}

