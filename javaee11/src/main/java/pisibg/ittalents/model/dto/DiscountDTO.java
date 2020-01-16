package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.pojo.Discount;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
public class DiscountDTO {
    private long id;
    @NotBlank(message = "This field should not be null")
    private String name;
    @NotNull
    private double amount;
    @Future
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_from;
    @Future
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_to;

    public DiscountDTO(Discount discount) {
        setId(discount.getId());
        setName(discount.getName());
        setAmount(discount.getAmount());
        setDate_from(discount.getDate_from());
        setDate_to(discount.getDate_to());
    }
}

