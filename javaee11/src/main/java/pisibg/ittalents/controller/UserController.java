package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import pisibg.ittalents.model.pojo.User;
import pisibg.ittalents.dao.UserDao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao dao;

    // if admin
    @GetMapping(value ="/users/all")
    public  List<User> getAll(){
        return dao.getAllUsers();
    }

    @PostMapping(value= "/users/register")
    public User registerUser(@RequestBody User u){

        return u;
    }


    @PostMapping("users/login")


    @GetMapping (value = "users/{name}")
    public User getByName(@PathVariable("name") String name){
        for (User u : getAll()) {
            if(u.getFirstName().equals(name)){
                return u;
            }
        }
        return null;
    }



}
