package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class DiscountDTO {
    private String name;
    private double amount;
    private LocalDate date_from;
    private LocalDate date_to;
}

