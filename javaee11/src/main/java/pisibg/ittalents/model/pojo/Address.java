package pisibg.ittalents.model.pojo;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//TODO table - database
@Table(name = "addresses")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String city;
    private String address_text;
    private String postal_code;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})

    @JoinTable(name = "user_has_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersAddresses = new HashSet<>();

    public void addAddressToUser(User user) {
        usersAddresses.add(user);
        user.getAddresses().add(this);
    }
    public void removeAddressToUser(User user) {
        usersAddresses.remove(user);
        user.getAddresses().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return getId() == address.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
