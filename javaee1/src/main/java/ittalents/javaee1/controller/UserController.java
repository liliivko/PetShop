package ittalents.javaee1.controller;

import model.User;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
public class UserController {



    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/users/add")
    public void addUser(@RequestBody User user) {
        // validation
        //  dao.save(user);

    }

    public void addAdmin() {

    }


    @PostMapping
    public void logIn(@RequestParam("email") String email) {

    }


    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleSQLException() {
        return "Sorry, something went wrong.Try again later";
    }

}