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

    private String first_name;
    private String last_name;
    private char gender;
    private String email;
    private boolean is_subscribed;

    public HiddenPasswordUserDTO(User user) {
        setFirst_name(user.getFirst_name());
        setLast_name(user.getLast_name());
        setGender(user.getGender());
        setEmail(user.getEmail());
        set_subscribed(user.is_subscribed());
    }

}
