package pisibg.ittalents.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pisibg.ittalents.model.pojo.User;

@Setter
@Getter
@NoArgsConstructor
public class HiddenPasswordUserDTO {
    private long id;
    private String first_name;
    private String last_name;
    private char gender;
    private String email;
    private boolean subscribed;

    public HiddenPasswordUserDTO(User user) {
        setId(user.getId());
        setFirst_name(user.getFirst_name());
        setLast_name(user.getLast_name());
        setGender(user.getGender());
        setEmail(user.getEmail());
        setSubscribed(user.getSubscribed());
    }
}
