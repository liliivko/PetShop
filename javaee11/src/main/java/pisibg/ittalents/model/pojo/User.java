package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.RegisterUserDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String first_name;
    @Column
    private String last_name;
    @Column
    private char gender;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private boolean is_admin;
    @Column
    private boolean is_subscribed;
//    @OneToMany(
//            mappedBy = "status",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//
//    private List<Order> orders = new ArrayList<>();
//
//    public void addOrder(Order order) {
//        orders.add(order);
//        order.setUser(this);
//    }
//
//    public void removeOrder(Order order) {
//        orders.remove(order);
//    }

    public User (RegisterUserDTO dto) {
        setFirst_name(dto.getFirst_name());
        setLast_name(dto.getLast_name());
        setGender(dto.getGender());
        setEmail(dto.getEmail());
        setPassword(dto.getPassword());
        set_subscribed(dto.is_subscribed());
    }

}