package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.pojo.Address;

@Setter
@Getter
@NoArgsConstructor
public class AddressDTO {
    long id;
    private String city;
    private String address_text;
    private String postal_code;


    public AddressDTO(Address address) {
        setId(address.getId());
        setCity(address.getCity());
        setAddress_text(address.getAddress_text());
        setPostal_code(address.getPostal_code());

    }
}
