package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.ittalents.model.dto.HiddenPasswordUserDTO;
import pisibg.ittalents.model.dto.RegisterUserDTO;
import pisibg.ittalents.model.pojo.User;
import org.springframework.web.bind.annotation.*;
import pisibg.ittalents.model.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;


@RestController
public class UserController {

  //  @Autowired
   // private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/users/register")
    public HiddenPasswordUserDTO register(@RequestBody RegisterUserDTO dto ) {
        // validate if the user exists and password validation
        // save to database
        User user= new User(dto);
        userRepository.save(user);
        HiddenPasswordUserDTO hiddenPasswordUserDTO = new HiddenPasswordUserDTO(user);
        return hiddenPasswordUserDTO;
    }

    @GetMapping(value = "/users")
    //if admin
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleSQLException() {
        return "Sorry, something went wrong.Try again later";
    }



}




