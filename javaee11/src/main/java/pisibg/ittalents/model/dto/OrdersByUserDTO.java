package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.pojo.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrdersByUserDTO {
    private long id;
    private Status status;
    private PaymentMethod paymentMethod;
    //TODO Address implementation in the order - JSON?
//    private Address address;
    private LocalDateTime createdOn;
    private double totalPrice;
    @JsonIgnore
    private HashMap<Product, Integer> products = new HashMap<>();

    OrdersByUserDTO (Order order){
        setId(order.getId());
        setStatus(order.getStatus());
        setPaymentMethod(order.getPaymentMethod());
        setCreatedOn(order.getCreatedOn());
        setTotalPrice(order.getTotalPrice());
    }
}
