package pisibg.ittalents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.beans.Transient;

@Getter
@Setter
public class User {
   private long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
     //password validation - passay library!
    @NotNull
    @JsonIgnore
    private String password;
    private boolean isAdmin;

    public User() {
        this.firstName = firstName;
    }

    public User(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

}
