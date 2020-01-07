package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class RegisterUserDTO {

    private String first_name;
    private String last_name;
    private char gender;
    private String email;
    private String password;
    private String confirmationPassword;
    private boolean is_admin;
    private boolean is_subscribed;

}
