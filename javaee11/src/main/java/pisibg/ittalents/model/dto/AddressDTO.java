package pisibg.ittalents.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.pojo.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class AddressDTO {
    long id;
    @NotBlank(message = "City may not be blank")
    private String city;
    @NotBlank(message = "Address may not be blank")
    private String address_text;
    @NotBlank(message = "Postal code may not be blank")
    private String postal_code;


    public AddressDTO(Address address) {
        setId(address.getId());
        setCity(address.getCity());
        setAddress_text(address.getAddress_text());
        setPostal_code(address.getPostal_code());

    }
}
