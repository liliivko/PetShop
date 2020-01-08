package pisibg.ittalents.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pisibg.ittalents.model.dto.RegisterUserDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//TODO table - database
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String first_name;
    @Column
    private String last_name;
    @Column
    private char gender;
    @Column
    private String email;
    @Column()
    private String password;
    @Column
    //TODO fix boolean
    private boolean is_admin;
    @Column
    private boolean is_subscribed;


    public User (RegisterUserDTO dto) {
        setFirst_name(dto.getFirst_name());
        setLast_name(dto.getLast_name());
        setGender(dto.getGender());
        setEmail(dto.getEmail());
        setPassword(dto.getPassword());
        set_subscribed(dto.is_subscribed());
    }


}