package pisibg.petshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pisibg.petshop.model.User;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static ArrayList<User> users = new ArrayList<>();
    static{
        users.add(new User("Pesho"));
        users.add(new User("Lili"));
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value ="/users/all")
    public  List<User> getAll(){
        List<User> users = jdbcTemplate.query("SELECT id, first_name, last_name, gender, email FROM users", (resultSet,i)-> toUser(resultSet));
        return users;
    }

    private User toUser(ResultSet resultSet) throws SQLException {
        User u = new User();
        u.setId(resultSet.getLong("id"));
        u.setFirstName(resultSet.getString("first_name"));
        u.setLastName(resultSet.getString("last_name"));
        u.setGender(resultSet.getString("gender"));
        u.setEmail(resultSet.getString("email"));
        return u;
    }

    @PostMapping(value= "/users/add")
    public void saveUser(@RequestBody User u){
        //validation! - error - return statuscode
        System.out.println(u.toString());
    }

    @GetMapping (value = "users/{name}")
    public User getByName(@PathVariable("name") String name){
        for (User u : users) {
            if(u.getFirstName().equals(name)){
                return u;
            }
        }
        return null;
    }

//    @PostMapping
//    public void login(HttpServletResponse resp){
//    resp.setStatus(418);
//    }
}
