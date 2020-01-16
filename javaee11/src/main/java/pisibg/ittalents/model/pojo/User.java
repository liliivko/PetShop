package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.RegisterUserDTO;

import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
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
    @JsonIgnore
    private String password;
    @Column
    private boolean isAdmin;
    @Column
    private Boolean subscribed;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "usersAddresses", cascade = CascadeType.MERGE)
    private Set<Address> addresses = new HashSet<>();

    public User(RegisterUserDTO dto) {
        setFirst_name(dto.getFirst_name());
        setLast_name(dto.getLast_name());
        setGender(dto.getGender());
        setEmail(dto.getEmail());
        setPassword(dto.getPassword());
        setSubscribed(true);
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