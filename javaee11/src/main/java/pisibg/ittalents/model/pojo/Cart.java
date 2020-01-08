package pisibg.ittalents.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.TreeMap;

@Getter
@Setter
@Component
@AllArgsConstructor
public class Cart {

    private User user;
    private boolean isEmpty;
    private double priceAtm;
    private HashMap <Product, Integer> items; //product and quantity


}
