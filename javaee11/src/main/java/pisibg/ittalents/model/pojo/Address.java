package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pisibg.ittalents.model.dto.AddressDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "City may not be blank")
    private String city;
    @NotBlank(message = "Address may not be blank")
    private String address_text;
    @NotBlank(message = "Postal_code may not be blank")
    private String postal_code;

    @ManyToMany(cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    @JsonIgnore
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


    public Address(AddressDTO address) {
        setCity(address.getCity());
        setAddress_text(address.getAddress_text());
        setPostal_code(address.getPostal_code());
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
