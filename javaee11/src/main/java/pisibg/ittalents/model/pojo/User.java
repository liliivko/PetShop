package pisibg.ittalents.model.pojo;
import lombok.*;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "usersAddresses",cascade = CascadeType.MERGE )
    private Set<Address> addresses= new HashSet<>();

    public User(RegisterUserDTO dto) {
        setFirst_name(dto.getFirst_name());
        setLast_name(dto.getLast_name());
        setGender(dto.getGender());
        setEmail(dto.getEmail());
        setPassword(dto.getPassword());
        set_subscribed(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}