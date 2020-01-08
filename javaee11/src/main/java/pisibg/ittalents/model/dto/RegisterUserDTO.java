package pisibg.ittalents.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor

public class RegisterUserDTO {

    private String first_name;
    private String last_name;
    private char gender;
    private String email;
    @NotNull
    @Size(min = 2, max = 14)
    private String password;
    private String confirmationPassword;
    private boolean is_subscribed;

}
