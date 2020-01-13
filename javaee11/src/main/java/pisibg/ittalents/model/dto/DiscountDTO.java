package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.pojo.Discount;
import pisibg.ittalents.model.pojo.User;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class DiscountDTO {
    private long id;
    private String name;
    private double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_from;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_to;
    //TODO constructor
    //TODO ratings HIDDEN pass user

    public DiscountDTO(Discount discount) {
        setId(discount.getId());
        setName(discount.getName());
        setAmount(discount.getAmount());
        setDate_from(discount.getDate_from());
        setDate_to(discount.getDate_to());
    }
}

