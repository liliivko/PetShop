package pisibg.ittalents.model.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.TreeMap;

@Getter
@Setter
public class Cart {

    private User user;
    private boolean isEmpty;
    private double priceAtm;
    private TreeMap <Product, Integer> items; //product and quantity




}
