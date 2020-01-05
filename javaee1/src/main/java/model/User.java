package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter

@NotBlank
public class User {


    private long id;
    private String firstName;
    private String lastName;
    private char gender;
    private String email;
    @JsonIgnore
    private String password;
    private boolean isAdmin;
    private boolean isSubscribed;


    public User(long id, String firstName, String lastName, char gender, String email,
                String password, boolean isSubscribed) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
        this.isSubscribed= isSubscribed;
    }




}
